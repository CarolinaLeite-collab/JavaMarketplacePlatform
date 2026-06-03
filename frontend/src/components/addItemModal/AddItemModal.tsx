import {Modal, Stepper, Button, Group} from '@mantine/core'
import { useState } from 'react'
import { PublicationStep } from './PublicationStep';
import {EditionStep} from './EditionStep';
import {ItemStep} from "./ItemStep.tsx";

interface AddItemModalProps {
    opened: boolean;
    onClose: () => void;
    onItemAdded: (item: any) => void;
}

    export function AddItemModal({
                                     opened,
                                     onClose,
                                     onItemAdded,
                                 }: AddItemModalProps) {

    const [activeStep, setActiveStep] = useState(0)

        const [publicationData, setPublicationData] = useState({
            title: '',
            authorName: '',
            releaseYear: 0,
            genreName: '',
        });

        const [editionData, setEditionData] = useState({
            identifier: '',
            publicationType: '',
            editionLanguage: '',
            publishingCompany: '',
            publishingYear: 0,
            weight: '',
            dimension: '',
            binding: '',
            numberOfPages: 0,
            editionNumber: 0,
        });

        const [itemData, setItemData] = useState({
            condition: '',
            description: '',
            picture: '',
        });

    const nextStep = () =>
        setActiveStep((current) => Math.min(current + 1, 3))

    const prevStep = () =>
        setActiveStep((current) => Math.max(current - 1, 0))

    const handleCreateItem = async () => {

        // TODO:
        // POST Publication
        // POST Edition
        // POST Item

        nextStep();
    };

        const handleAddToLibrary = () => {
            const newItem = {
                itemId: `ITEM-${Date.now()}`,
                title: publicationData.title,
                imageUrl: itemData.picture || null,
                publicationType: editionData.publicationType,
                authorName: publicationData.authorName,
                identifier: editionData.identifier,
            };

            onItemAdded(newItem);
            resetForm();
        };

        const resetForm = () => {
            setActiveStep(0);

            setPublicationData({
                title: '',
                authorName: '',
                releaseYear: 0,
                genreName: '',
            });

            setEditionData({
                identifier: '',
                publicationType: '',
                editionLanguage: '',
                publishingCompany: '',
                publishingYear: 0,
                weight: '',
                dimension: '',
                binding: '',
                numberOfPages: 0,
                editionNumber: 0,
            });

            setItemData({
                condition: '',
                description: '',
                picture: '',
            });
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
                    />
                </Stepper.Step>

                <Stepper.Step label="Edition">
                    <EditionStep
                        data={editionData}
                        setData={setEditionData}
                    />
                </Stepper.Step>

                <Stepper.Step label="Item">
                    <ItemStep
                        data={itemData}
                        setData={setItemData}
                    />
                </Stepper.Step>

                <Stepper.Completed>
                    Item Successfully Registered!
                    You can now add it to your library.
                </Stepper.Completed>
            </Stepper>

            <Group justify="space-between" mt="md">
                <Button
                    variant="default"
                    onClick={prevStep}
                    disabled={activeStep === 0}
                >
                    Back
                </Button>

                {activeStep < 2 && (
                    <Button onClick={nextStep}>
                        Next
                    </Button>
                )}

                {activeStep === 2 && (
                    <Button onClick={handleCreateItem}>
                        Create Item
                    </Button>
                )}

                {activeStep === 3 && (
                    <Button onClick={handleAddToLibrary}>
                        Add to Library
                    </Button>
                )}
            </Group>
        </Modal>
    );
    }