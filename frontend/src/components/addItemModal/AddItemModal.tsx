import {Modal, Stepper, Button, Group, Text} from '@mantine/core'
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
 * adds the created item to the user's library. Each step's required fields are
 * validated before the user is allowed to advance to the next step, and all
 * three steps are re-validated before final submission. Submission errors
 * returned by the backend are surfaced inline instead of failing silently.
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

    const [publicationErrors, setPublicationErrors] = useState<Record<string, string>>({});
    const [editionErrors, setEditionErrors] = useState<Record<string, string>>({});
    const [itemErrors, setItemErrors] = useState<Record<string, string>>({});
    const [submitError, setSubmitError] = useState<string | null>(null);

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

    /**
     * Validates the Publication step's required fields.
     *
     * @returns `true` if all required fields are filled in; `false` otherwise.
     * As a side effect, populates `publicationErrors` with a message per invalid field.
     */
    function validatePublicationStep() {
        const errors: Record<string, string> = {};

        if (!publicationData.title.trim()) {
            errors.title = 'Title is required';
        }
        if (!publicationData.authorName) {
            errors.authorName = 'Author is required';
        }
        if (!publicationData.releaseYear || publicationData.releaseYear <= 0) {
            errors.releaseYear = 'Release year is required';
        }
        if (!publicationData.genreName) {
            errors.genreName = 'Genre is required';
        }

        setPublicationErrors(errors);
        return Object.keys(errors).length === 0;
    }

    /**
     * Validates the Edition step's required fields.
     *
     * @returns `true` if all required fields are filled in; `false` otherwise.
     * As a side effect, populates `editionErrors` with a message per invalid field.
     */
    function validateEditionStep() {
        const errors: Record<string, string> = {};

        if (!editionData.publicationTypeId) {
            errors.publicationTypeId = 'Publication type is required';
        }
        if (!editionData.language) {
            errors.language = 'Language is required';
        }
        if (!editionData.publishingCompanyId) {
            errors.publishingCompanyId = 'Publishing company is required';
        }
        if (!editionData.publishingYear || editionData.publishingYear <= 0) {
            errors.publishingYear = 'Publishing year is required';
        }

        setEditionErrors(errors);
        return Object.keys(errors).length === 0;
    }

    /**
     * Validates the Item step's required fields.
     *
     * @returns `true` if all required fields are filled in; `false` otherwise.
     * As a side effect, populates `itemErrors` with a message per invalid field.
     */
    function validateItemStep() {
        const errors: Record<string, string> = {};

        if (!itemData.condition) {
            errors.condition = 'Condition is required';
        }
        if (!itemData.description.trim()) {
            errors.description = 'Description is required';
        }

        setItemErrors(errors);
        return Object.keys(errors).length === 0;
    }

    /**
     * Extracts a user-facing message from an error thrown by the API client.
     *
     * Backend errors arrive as a raw JSON string in `error.message` (e.g.
     * `{"status":"400 BAD_REQUEST","message":"..."}`); this safely parses it
     * and falls back to the raw message if parsing fails.
     */
    function extractErrorMessage(error: unknown): string {
        const raw = error instanceof Error ? error.message : String(error);

        try {
            const parsed = JSON.parse(raw);
            return parsed.message ?? raw;
        } catch {
            return raw;
        }
    }

    const nextStep = () => {
        if (activeStep === 0 && !validatePublicationStep()) {
            return;
        }

        if (activeStep === 1 && !validateEditionStep()) {
            return;
        }

        setActiveStep((current) => Math.min(current + 1, 3));
    };

    const prevStep = () =>
        setActiveStep((current) => Math.max(current - 1, 0))

    function withQueryParam(href: string, key: string, value: string) {
        const url = new URL(href);
        url.searchParams.set(key, value);
        return url.toString();
    }

    const handleCreateItem = async () => {
        setSubmitError(null);

        try {
            const isPublicationValid = validatePublicationStep();
            const isEditionValid = validateEditionStep();
            const isItemValid = validateItemStep();

            if (!isPublicationValid || !isEditionValid || !isItemValid) {
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
            setSubmitError(extractErrorMessage(error));
        }
    };

    const resetForm = () => {
        setActiveStep(0);
        setCreatedItem(null);
        setPublicationErrors({});
        setEditionErrors({});
        setItemErrors({});
        setSubmitError(null);

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
                        errors={publicationErrors}
                    />
                </Stepper.Step>

                <Stepper.Step label="Edition">
                    <EditionStep
                        data={editionData}
                        setData={setEditionData}
                        publicationTypes={publicationTypes}
                        publishingCompanies={publishingCompanies}
                        errors={editionErrors}
                    />
                </Stepper.Step>

                <Stepper.Step label="Item">
                    <ItemStep
                        data={itemData}
                        setData={setItemData}
                        errors={itemErrors}
                    />
                </Stepper.Step>

                <Stepper.Completed>
                    Item Successfully Registered and added to your Library!
                </Stepper.Completed>
            </Stepper>

            {submitError && (
                <Text c="red" size="sm" mt="md">
                    {submitError}
                </Text>
            )}

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