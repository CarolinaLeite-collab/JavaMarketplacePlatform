import {Modal, Stepper, Button, Group} from '@mantine/core'
import { useContext,useEffect, useState } from 'react'
import { PublicationStep } from './PublicationStep';
import {EditionStep} from './EditionStep';
import {ItemStep} from "./ItemStep.tsx";
import { apiClient } from '../../services/apiClient';
import AppContext from '../../context/AppContext';

interface AddItemModalProps {
    opened: boolean;
    onClose: () => void;
    onItemAdded: (item: any) => void;
}

/**
 * Modal responsible for registering a new item and adding it to the user's library.
 *
 * The workflow is split into publication, edition, and item steps. Required backend
 * resources are discovered through HATEOAS links stored in AppContext. The modal
 * creates or reuses the publication and edition, creates the item, and finally
 * adds the created item to the user's library.
 */

    export function AddItemModal({
                                     opened,
                                     onClose,
                                     onItemAdded,
                                 }: AddItemModalProps) {

        const {state} = useContext(AppContext);

        const {
            createPublicationHref,
            createEditionHref,
            createItemHref,
            libraryAddHref,
            authorsHref,
            genresHref,
            publicationTypesHref,
            publishingCompaniesHref,
        } = state.app;

        const [authors, setAuthors] = useState<{ value: string; label: string }[]>([]);
        const [genres, setGenres] = useState<{ value: string; label: string }[]>([]);
        const [publicationTypes, setPublicationTypes] = useState<{ value: string; label: string }[]>([]);
        const [publishingCompanies, setPublishingCompanies] = useState<{ value: string; label: string }[]>([]);

        const [activeStep, setActiveStep] = useState(0)

        const [createdItem, setCreatedItem] = useState<any>(null);

        const [publicationData, setPublicationData] = useState({
            title: '',
            authorName: '',
            releaseYear: 1000,
            genreName: '',
        });

        const [editionData, setEditionData] = useState({
            publicationTypeId: '',
            publishingCompanyId: '',
            publishingYear: 0,
            language: '',
            identifier: '',
            dimension: {
                width: 0,
                height: 0,
                thickness: 0,
                unit: '',
            },
            weight: {
                value: 0,
                unit: '',
            },

            numberOfPages: 0,
            editionNumber: 0,
            binding: '',
        });

        const [itemData, setItemData] = useState({
            condition: '',
            description: '',
            picture: '',
        });

        useEffect(() => {
            if (!opened) return;

            async function loadOptions() {
                try {
                    const [authorsData, genresData, publicationTypesData, publishingCompaniesData] =
                        await Promise.all([
                            authorsHref ? apiClient.getByHref(authorsHref) : [],
                            genresHref ? apiClient.getByHref(genresHref) : [],
                            publicationTypesHref ? apiClient.getByHref(publicationTypesHref) : [],
                            publishingCompaniesHref ? apiClient.getByHref(publishingCompaniesHref) : [],
                        ]);

                    setAuthors((authorsData ?? []).map((author: any) => ({
                        value: author.authorId,
                        label: author.authorName ?? author.name ?? author.authorId,
                    })));

                    setGenres((genresData ?? []).map((genre: any) => ({
                        value: genre.genreId,
                        label: genre.genreName ?? genre.name ?? genre.genreId,
                    })));

                    setPublicationTypes((publicationTypesData ?? []).map((type: any) => ({
                        value: type.publicationTypeId,
                        label: type.publicationTypeName ?? type.name ?? type.publicationTypeId,
                    })));

                    setPublishingCompanies((publishingCompaniesData ?? []).map((company: any) => ({
                        value: company.publishingCompanyId,
                        label: company.name ?? company.publishingCompanyName ?? company.publishingCompanyId,
                    })));
                } catch (error) {
                    console.error('Failed to load add item options:', error);
                }
            }

            loadOptions();
        }, [opened, authorsHref, genresHref, publicationTypesHref, publishingCompaniesHref]);

        const nextStep = () =>
            setActiveStep((current) => Math.min(current + 1, 3))

        const prevStep = () =>
            setActiveStep((current) => Math.max(current - 1, 0))

        function withQueryParam(href: string, key: string, value: string) {
            const url = new URL(href);
            url.searchParams.set(key, value);
            return url.toString();
        }

        const handleCreateItem = async () => {
            try {
                if (
                    !publicationData.title ||
                    !publicationData.authorName ||
                    !publicationData.genreName ||
                    !editionData.publicationTypeId ||
                    !editionData.publishingCompanyId ||
                    !editionData.publishingYear ||
                    !editionData.language ||
                    !itemData.condition ||
                    !itemData.description
                ) {
                    alert('Please fill all required fields.');
                    return;
                }

                const publicationPayload = {
                    title: publicationData.title,
                    authorId: publicationData.authorName,
                    releaseYear: Number(publicationData.releaseYear),
                    genreId: publicationData.genreName,
                };

                const publication = await apiClient.postByHref(
                    createPublicationHref,
                    publicationPayload
                );

                const publicationId = decodeURIComponent(apiClient.extractIdFromSelfLink(publication));

                const editionPayload: any = {
                    publicationTypeId: editionData.publicationTypeId,
                    publishingCompanyId: editionData.publishingCompanyId,
                    publishingYear: editionData.publishingYear,
                    language: editionData.language,
                };

                if (editionData.identifier) {
                    editionPayload.identifier = editionData.identifier;
                }

                if (editionData.numberOfPages) {
                    editionPayload.numberOfPages = editionData.numberOfPages;
                }

                if (editionData.editionNumber) {
                    editionPayload.editionNumber = editionData.editionNumber;
                }

                if (editionData.binding) {
                    editionPayload.binding = editionData.binding;
                }

                if (
                    editionData.dimension.unit &&
                    editionData.dimension.width &&
                    editionData.dimension.height &&
                    editionData.dimension.thickness
                ) {
                    editionPayload.dimension = editionData.dimension;
                }

                if (editionData.weight.unit && editionData.weight.value) {
                    editionPayload.weight = editionData.weight;
                }

                const edition = await apiClient.postByHref(
                    withQueryParam(createEditionHref, 'pubId', publicationId),
                    editionPayload
                );

                const editionId = decodeURIComponent(apiClient.extractIdFromSelfLink(edition));

                const item = await apiClient.postByHref(
                    createItemHref,
                    {
                        editionId,
                        condition: itemData.condition,
                        description: itemData.description,
                    }
                );

                await apiClient.postByHref(libraryAddHref, {
                    itemId: item.itemId,
                });

                setCreatedItem(item);
                setActiveStep(3);

            } catch (error) {
                console.error(error);
            }
        };

        const resetForm = () => {
            setActiveStep(0);
            setCreatedItem(null);


            setPublicationData({
                title: '',
                authorName: '',
                releaseYear: 1000,
                genreName: '',
            });

            setEditionData({
                publicationTypeId: '',
                publishingCompanyId: '',
                publishingYear: 0,
                language: '',
                identifier: '',
                dimension: {
                    width: 0,
                    height: 0,
                    thickness: 0,
                    unit: '',
                },
                weight: {
                    value: 0,
                    unit: '',
                },
                numberOfPages: 0,
                editionNumber: 0,
                binding: '',
            });

            setItemData({
                condition: '',
                description: '',
                picture: '',
            });
        };

        const handleDone = () => {
            if (createdItem) {
                onItemAdded(createdItem);
            }

            resetForm();
        };

        const handleClose = () => {
            resetForm();
            onClose();
        };

        return (
            <Modal
                opened={opened}
                onClose={handleClose}
                size="xl"
                title="Add Item"
            >
                <Stepper active={activeStep}>
                    <Stepper.Step label="Publication">
                        <PublicationStep
                            data={publicationData}
                            setData={setPublicationData}
                            authors={authors}
                            genres={genres}
                        />
                    </Stepper.Step>

                    <Stepper.Step label="Edition">
                        <EditionStep
                            data={editionData}
                            setData={setEditionData}
                            publicationTypes={publicationTypes}
                            publishingCompanies={publishingCompanies}
                        />
                    </Stepper.Step>

                    <Stepper.Step label="Item">
                        <ItemStep
                            data={itemData}
                            setData={setItemData}
                        />
                    </Stepper.Step>

                    <Stepper.Completed>
                        Item Successfully Registered and added to your Library!
                    </Stepper.Completed>
                </Stepper>

                <Group justify="space-between" mt="md">
                    <Button
                        variant="default"
                        onClick={prevStep}
                        disabled={activeStep === 0 || activeStep === 3}
                    >
                        Back
                    </Button>

                    {activeStep < 2 && (
                        <Button onClick={nextStep}>
                            Next
                        </Button>
                    )}

                    {activeStep === 2 && (
                        <Button
                            onClick={handleCreateItem}
                            disabled={!createPublicationHref || !createEditionHref || !createItemHref || !libraryAddHref}
                        >
                            Create Item
                        </Button>
                    )}

                    {activeStep === 3 && (
                        <Button onClick={handleDone}>
                            Done
                        </Button>
                    )}
                </Group>
            </Modal>
        );
    }