import { describe, it, expect, vi, beforeEach } from 'vitest';
import { apiClient } from '../services/apiClient';
import {
    GET_LIBRARY_ITEMS_SUCCESS,
    GET_LIBRARY_ITEMS_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    CREATE_DIRECT_SALE_ERROR,
    CLEAR_SALES_MESSAGES,
    getLibraryItemsSuccess,
    getLibraryItemsError,
    createDirectSaleSuccess,
    createDirectSaleError,
    clearSalesMessages,
    getMyLibraryItems,
    createDirectSale,
} from '../context/sales/SalesActions.jsx';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getMyLibraryItemsForSale: vi.fn(),
        postByHref: vi.fn(),
    },
}));

describe('SalesActions', () => {
    const dispatch = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('action creators', () => {
        it('getLibraryItemsSuccess returns correct action', () => {
            const payload = [{ itemId: '1', title: 'Dune' }];

            expect(getLibraryItemsSuccess(payload)).toEqual({
                type: GET_LIBRARY_ITEMS_SUCCESS,
                payload,
            });
        });

        it('getLibraryItemsError returns correct action', () => {
            const error = 'Failed to load items';

            expect(getLibraryItemsError(error)).toEqual({
                type: GET_LIBRARY_ITEMS_ERROR,
                payload: error,
            });
        });

        it('createDirectSaleSuccess returns correct action', () => {
            const payload = { directSaleId: 'sale-1' };

            expect(createDirectSaleSuccess(payload)).toEqual({
                type: CREATE_DIRECT_SALE_SUCCESS,
                payload,
            });
        });

        it('createDirectSaleError returns correct action', () => {
            const error = 'Failed to create sale';

            expect(createDirectSaleError(error)).toEqual({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: error,
            });
        });

        it('clearSalesMessages returns correct action', () => {
            expect(clearSalesMessages()).toEqual({
                type: CLEAR_SALES_MESSAGES,
            });
        });
    });

    describe('getMyLibraryItems', () => {
        it('dispatches success action when api call succeeds', async () => {
            const result = [
                { itemId: '1', title: 'Dune', links: [] },
            ];

            apiClient.getMyLibraryItemsForSale.mockResolvedValue(result);

            await getMyLibraryItems(dispatch);

            expect(apiClient.getMyLibraryItemsForSale).toHaveBeenCalledTimes(1);
            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_ITEMS_SUCCESS,
                payload: result,
            });
        });

        it('dispatches error action when api call fails with plain error', async () => {
            apiClient.getMyLibraryItemsForSale.mockRejectedValue(new Error('Network error'));

            await getMyLibraryItems(dispatch);

            expect(apiClient.getMyLibraryItemsForSale).toHaveBeenCalledTimes(1);
            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_ITEMS_ERROR,
                payload: 'Network error',
            });
        });

        it('dispatches parsed error message when api call fails with JSON error message', async () => {
            apiClient.getMyLibraryItemsForSale.mockRejectedValue(
                new Error(JSON.stringify({ message: 'Library fetch failed' }))
            );

            await getMyLibraryItems(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_ITEMS_ERROR,
                payload: 'Library fetch failed',
            });
        });
    });

    describe('createDirectSale', () => {
        it('dispatches error action and returns false when href is missing', async () => {
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            const success = await createDirectSale(dispatch, null, body);

            expect(apiClient.postByHref).not.toHaveBeenCalled();
            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: 'Missing create-direct-sale link.',
            });
            expect(success).toBe(false);
        });

        it('dispatches success action and returns true when api call succeeds', async () => {
            const href = 'http://localhost:8081/direct-sales';
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            const result = { directSaleId: 'sale-123' };

            apiClient.postByHref.mockResolvedValue(result);

            const success = await createDirectSale(dispatch, href, body);

            expect(apiClient.postByHref).toHaveBeenCalledTimes(1);
            expect(apiClient.postByHref).toHaveBeenCalledWith(href, body);
            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_SUCCESS,
                payload: result,
            });
            expect(success).toBe(true);
        });

        it('dispatches error action and returns false when api call fails with plain error', async () => {
            const href = 'http://localhost:8081/direct-sales';
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            apiClient.postByHref.mockRejectedValue(new Error('Create sale failed'));

            const success = await createDirectSale(dispatch, href, body);

            expect(apiClient.postByHref).toHaveBeenCalledWith(href, body);
            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: 'Create sale failed',
            });
            expect(success).toBe(false);
        });

        it('dispatches parsed error action and returns false when api call fails with JSON error', async () => {
            const href = 'http://localhost:8081/direct-sales';
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            apiClient.postByHref.mockRejectedValue(
                new Error(JSON.stringify({ message: 'Direct sale creation failed' }))
            );

            const success = await createDirectSale(dispatch, href, body);

            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: 'Direct sale creation failed',
            });
            expect(success).toBe(false);
        });
    });
});