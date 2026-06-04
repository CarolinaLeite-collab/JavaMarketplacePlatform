import { useContext, useEffect, useState } from "react";
import { notifications } from '@mantine/notifications';
import {
    Alert,
    Button,
    Group,
    Modal,
    NumberInput,
    Select,
    Stack,
} from "@mantine/core";
import { IconCheck } from "@tabler/icons-react";
import AppContext from "../../context/AppContext";
import {
    clearSalesMessages,
    createDirectSale,
    getMyLibraryItems
} from "../../context/sales/SalesActions.jsx";

export function CreateSaleModal({ opened, onClose }) {
    const { state, dispatch } = useContext(AppContext);
    const { libraryItems, error, successMessage } = state.sales;

    const [saleType, setSaleType] = useState("Direct sale");
    const [itemId, setItemId] = useState<string | null>(null);
    const [priceValue, setPriceValue] = useState<string | number>("");
    const [priceCurrency, setPriceCurrency] = useState("EUR");
    const [durationDays, setDurationDays] = useState<string | number>("");
    const [fieldErrors, setFieldErrors] = useState<Record<string, string | undefined>>({});

    useEffect(() => {
        if (opened) {
            dispatch(clearSalesMessages());
            getMyLibraryItems(dispatch);
        }
    }, [opened, dispatch]);

    const resetForm = () => {
        setSaleType("Direct sale");
        setItemId(null);
        setPriceValue("");
        setPriceCurrency("EUR");
        setDurationDays("");
        setFieldErrors({});
    };

    const handleClose = () => {
        resetForm();
        onClose();
    };

    const validateFields = () => {
        const errors: Record<string, string> = {};

        if (!saleType) errors.saleType = "Sale type is required.";
        if (!itemId) errors.itemId = "Item is required.";
        if (priceValue === "" || Number(priceValue) <= 0) {
            errors.priceValue = "Price value is required and must be greater than 0.";
        }
        if (!priceCurrency) errors.priceCurrency = "Currency is required.";

        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleCreate = async () => {
        if (!validateFields()) return;

        const selectedItem = libraryItems.find((item) => item.value === itemId);
        const href = selectedItem?.createDirectSaleHref ?? null;

        if (!href) {
            setFieldErrors((prev) => ({
                ...prev,
                itemId: "This item cannot be put on direct sale.",
            }));
            return;
        }

        const body: any = {
            itemsId: [itemId],
            priceValue: Number(priceValue),
            priceCurrency,
        };

        if (durationDays !== "" && durationDays !== null) {
            body.timeLimitSeconds = Number(durationDays) * 24 * 60 * 60;
        }

        const success = await createDirectSale(dispatch, href, body);

        if (success) {
            handleClose();
            notifications.show({
                title: 'Direct sale created',
                message: 'The item was successfully put on direct sale.',
                color: 'green',
                autoClose: 3000,
            });
        }
    };

    return (
        <Modal
            opened={opened}
            onClose={handleClose}
            title="Create New Sale"
            overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
            centered
        >
            <Stack>
                {error && <Alert color="red">{error}</Alert>}
                {successMessage && (
                    <Alert icon={<IconCheck size={16} />} color="green">
                        {successMessage}
                    </Alert>
                )}

                <Select
                    label="Sale type"
                    withAsterisk
                    placeholder="Select the type of sale"
                    data={[{ value: "Direct sale", label: "Direct sale" }]}
                    value={saleType}
                    onChange={setSaleType}
                    allowDeselect={false}
                    error={fieldErrors.saleType}
                />

                <Select
                    label="Item"
                    withAsterisk
                    placeholder="Select an item from your library"
                    data={libraryItems}
                    value={itemId}
                    onChange={(value) => {
                        setItemId(value);

                        if (fieldErrors.itemId) {
                            setFieldErrors((prev) => ({
                                ...prev,
                                itemId: undefined,
                            }));
                        }
                    }}
                    searchable
                    nothingFoundMessage="No items found in your library"
                    error={fieldErrors.itemId}
                />

                <Group grow align="flex-end">
                    <NumberInput
                        label="Price value"
                        withAsterisk
                        placeholder="Enter price"
                        min={0}
                        decimalScale={2}
                        fixedDecimalScale
                        value={priceValue}
                        onChange={(value) => {
                            setPriceValue(value ?? "");
                            if (fieldErrors.priceValue && Number(value) > 0) {
                                setFieldErrors((prev) => ({
                                    ...prev,
                                    priceValue: undefined,
                                }));
                            }
                        }}
                        error={fieldErrors.priceValue}
                    />

                    <Select
                        label="Currency"
                        withAsterisk
                        placeholder="Currency"
                        data={[
                            { value: "EUR", label: "EUR" },
                            { value: "USD", label: "USD" },
                            { value: "GBP", label: "GBP" },
                        ]}
                        value={priceCurrency}
                        onChange={setPriceCurrency}
                        allowDeselect={false}
                        error={fieldErrors.priceCurrency}
                    />
                </Group>

                <NumberInput
                    label="Duration (days)"
                    placeholder="Enter duration (optional)"
                    min={1}
                    value={durationDays}
                    onChange={setDurationDays}
                />

                <Button fullWidth mt="sm" color="indigo" radius="xl" onClick={handleCreate}>
                    Create Sale
                </Button>
            </Stack>
        </Modal>
    );
}