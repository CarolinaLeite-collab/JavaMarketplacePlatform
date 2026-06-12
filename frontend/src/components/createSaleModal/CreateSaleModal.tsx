import {useContext, useEffect, useState} from "react";
import {notifications} from '@mantine/notifications';
import {Alert, Button, Group, Input, Modal, MultiSelect, NumberInput, Select, Stack,} from "@mantine/core";
import {IconCheck} from "@tabler/icons-react";
import AppContext from "../../context/AppContext";
import {clearSalesMessages, createDirectSale, createAuction, getMyLibraryItems} from "../../context/sales/SalesActions.jsx";


export function CreateSaleModal({ opened, onClose }) {
    const { state, dispatch } = useContext(AppContext);
    const { libraryItems, error, successMessage } = state.sales;

    const [saleType, setSaleType] = useState("Direct sale");
    const [itemId, setItemId] = useState<string | null>(null);
    const [itemIds, setItemIds] = useState<string[]>([]);
    const [priceValue, setPriceValue] = useState<string | number>("");
    const [priceCurrency, setPriceCurrency] = useState("EUR");
    const [durationDays, setDurationDays] = useState<string | number>("");

    const [startingPriceValue, setStartingPriceValue] = useState<string | number>("");
    const [reservePriceValue, setReservePriceValue] = useState<string | number>("");
    const [auctionCurrency, setAuctionCurrency] = useState("EUR");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [outrightPriceValue, setOutrightPriceValue] = useState<string | number>("");


    const [fieldErrors, setFieldErrors] = useState<Record<string, string | undefined>>({});

    useEffect(() => {
        if (opened) {
            dispatch(clearSalesMessages());
            getMyLibraryItems(dispatch);
        }
    }, [opened, dispatch]);

    const resetForm = () => {
        setSaleType("Direct sale");
        setItemId(null)
        setItemIds([]);
        setPriceValue("");
        setPriceCurrency("EUR");
        setDurationDays("");
        setStartingPriceValue("");
        setReservePriceValue("");
        setAuctionCurrency("EUR");
        setStartDate("");
        setEndDate("");
        setOutrightPriceValue("");
        setFieldErrors({});
    };

    const handleClose = () => {
        resetForm();
        onClose();
    };

    const availableItems = libraryItems.filter((item) =>
        saleType === "Auction" ? !!item.createAuctionHref : !!item.createDirectSaleHref
    );

    const handleSaleTypeChange = (value: string | null) => {
        setSaleType(value);
        setItemId(null); // reset item when type changes — different eligible sets
        setItemIds([]);
        setFieldErrors({});
    };

    const validateDirectSale = () => {
        const errors: Record<string, string> = {};
        if (!itemId) errors.itemId = "Item is required.";
        if (priceValue === "" || Number(priceValue) <= 0)
            errors.priceValue = "Price must be greater than 0.";
        if (!priceCurrency) errors.priceCurrency = "Currency is required.";
        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const validateAuction = () => {
        const errors: Record<string, string> = {};
        if (!itemIds || itemIds.length === 0) errors.itemId = "At least one item is required.";
        if (startingPriceValue === "" || Number(startingPriceValue) <= 0)
            errors.startingPriceValue = "Starting price must be greater than 0.";
        if (reservePriceValue === "" || Number(reservePriceValue) <= 0)
            errors.reservePriceValue = "Reserve price must be greater than 0.";
        if (Number(reservePriceValue) < Number(startingPriceValue))
            errors.reservePriceValue = "Reserve price must be ≥ starting price.";
        if (!auctionCurrency) errors.auctionCurrency = "Currency is required.";
        if (!startDate) errors.startDate = "Start date is required.";
        if (!endDate) errors.endDate = "End date is required.";
        if (startDate && endDate && new Date(endDate) <= new Date(startDate))
            errors.endDate = "End date must be after start date.";
        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleCreate = async () => {
        if (saleType === "Auction") {
            if (!validateAuction()) return;

            const selectedItem = libraryItems.find((item) => item.value === itemIds[0]);
            const href = selectedItem?.createAuctionHref ?? null;

            if (!href) {
                setFieldErrors((prev) => ({ ...prev, itemId: "This item cannot be put on auction." }));
                return;
            }

            const body = {
                itemIds: itemIds,
                startingPrice: Number(startingPriceValue),
                reservePrice: Number(reservePriceValue),
                outrightPrice: outrightPriceValue !== "" ? Number(outrightPriceValue) : null,
                priceCurrency: auctionCurrency,
                startDate: new Date(startDate).toISOString(),
                endDate: new Date(endDate).toISOString(),
            };

            const success = await createAuction(dispatch, href, body);

            if (success) {
                handleClose();
                notifications.show({
                    title: 'Auction created',
                    message: 'The item was successfully put on auction.',
                    color: 'green',
                    autoClose: 3000,
                });
            }
        } else {
            if (!validateDirectSale()) return;

            const selectedItem = libraryItems.find((item) => item.value === itemId);
            const href = selectedItem?.createDirectSaleHref ?? null;

            if (!href) {
                setFieldErrors((prev) => ({ ...prev, itemId: "This item cannot be put on direct sale." }));
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
                    data={[
                        { value: "Direct sale", label: "Direct sale" },
                        { value: "Auction", label: "Auction" },
                    ]}
                    value={saleType}
                    onChange={handleSaleTypeChange}
                    allowDeselect={false}
                    error={fieldErrors.saleType}
                />

                {saleType === "Direct sale" ? (
                    <Select
                        label="Item"
                        withAsterisk
                        placeholder="Select an item from your library"
                        data={availableItems}
                        value={itemId}
                        onChange={(value) => {
                            setItemId(value);
                            if (fieldErrors.itemId) {
                                setFieldErrors((prev) => ({ ...prev, itemId: undefined }));
                            }
                        }}
                        searchable
                        nothingFoundMessage="No items available for this sale type"
                        error={fieldErrors.itemId}
                    />
                ) : (
                    <MultiSelect
                        label="Items"
                        withAsterisk
                        placeholder={itemIds.length === 0 ? "Select items from your library" : ""}
                        data={availableItems}
                        value={itemIds}
                        onChange={(values) => {
                            setItemIds(values);
                            if (fieldErrors.itemId) {
                                setFieldErrors((prev) => ({ ...prev, itemId: undefined }));
                            }
                        }}
                        searchable
                        hidePickedOptions
                        nothingFoundMessage="No items available for this sale type"
                        error={fieldErrors.itemId}
                        comboboxProps={{
                            shadow: "xl",
                            withinPortal: true,
                            styles: {
                                dropdown: {
                                    border: '2px solid #339af0',
                                    borderRadius: 8,
                                }
                            }
                        }}
                    />
                )}

                {/* ── Direct sale fields ── */}
                {saleType === "Direct sale" && (
                    <>
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
                                    if (fieldErrors.priceValue && Number(value) > 0)
                                        setFieldErrors((prev) => ({ ...prev, priceValue: undefined }));
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
                    </>
                )}

                {/* ── Auction fields ── */}
                {saleType === "Auction" && (
                    <>
                        <Group grow align="flex-end">
                            <NumberInput
                                label="Starting price"
                                withAsterisk
                                placeholder="Enter starting price"
                                min={0}
                                decimalScale={2}
                                fixedDecimalScale
                                value={startingPriceValue}
                                onChange={(value) => {
                                    setStartingPriceValue(value ?? "");
                                    if (fieldErrors.startingPriceValue && Number(value) > 0)
                                        setFieldErrors((prev) => ({ ...prev, startingPriceValue: undefined }));
                                }}
                                error={fieldErrors.startingPriceValue}
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
                                value={auctionCurrency}
                                onChange={setAuctionCurrency}
                                allowDeselect={false}
                                error={fieldErrors.auctionCurrency}
                            />
                        </Group>

                        <NumberInput
                            label="Reserve price"
                            withAsterisk
                            placeholder="Enter reserve price"
                            min={0}
                            decimalScale={2}
                            fixedDecimalScale
                            value={reservePriceValue}
                            onChange={(value) => {
                                setReservePriceValue(value ?? "");
                                if (fieldErrors.reservePriceValue)
                                    setFieldErrors((prev) => ({ ...prev, reservePriceValue: undefined }));
                            }}
                            error={fieldErrors.reservePriceValue}
                        />

                        <NumberInput
                            label="Outright price (optional)"
                            placeholder="Enter buy now price"
                            min={0}
                            decimalScale={2}
                            fixedDecimalScale
                            value={outrightPriceValue}
                            onChange={(value) => setOutrightPriceValue(value ?? "")}
                        />

                        <Group grow>
                            <div>
                                <Input.Label required>Start date</Input.Label>
                                <input
                                    type="datetime-local"
                                    value={startDate}
                                    onChange={(e) => {
                                        setStartDate(e.target.value);
                                        if (fieldErrors.startDate)
                                            setFieldErrors((prev) => ({ ...prev, startDate: undefined }));
                                    }}
                                    style={{
                                        padding: '8px 12px',
                                        borderRadius: 6,
                                        border: fieldErrors.startDate ? '1px solid red' : '1px solid #ced4da',
                                        fontSize: 14,
                                        width: '100%',
                                    }}
                                />
                            </div>
                            <div>
                                <Input.Label required>End date</Input.Label>
                                <input
                                    type="datetime-local"
                                    value={endDate}
                                    onChange={(e) => {
                                        setEndDate(e.target.value);
                                        if (fieldErrors.endDate)
                                            setFieldErrors((prev) => ({ ...prev, endDate: undefined }));
                                    }}
                                    style={{
                                        padding: '8px 12px',
                                        borderRadius: 6,
                                        border: fieldErrors.endDate ? '1px solid red' : '1px solid #ced4da',
                                        fontSize: 14,
                                        width: '100%',
                                    }}
                                />
                            </div>
                        </Group>
                        {(fieldErrors.startDate || fieldErrors.endDate) && (
                            <Alert color="red" p="xs">
                                {fieldErrors.startDate || fieldErrors.endDate}
                            </Alert>
                        )}
                    </>
                )}

                <Button fullWidth mt="sm" color="indigo" radius="xl" onClick={handleCreate}>
                    Create Sale
                </Button>
            </Stack>
        </Modal>
    );
}
