import {beforeEach, describe, expect, it, vi} from 'vitest';
import {apiClient} from '../services/apiClient';
import {
    CLEAR_SALES_MESSAGES,
    clearSalesMessages,
    CREATE_DIRECT_SALE_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    createDirectSale,
    createDirectSaleError,
    createDirectSaleSuccess,
    GET_LIBRARY_ITEMS_ERROR,
    GET_LIBRARY_ITEMS_SUCCESS,
    getLibraryItemsError,
    getLibraryItemsSuccess,
    getMyLibraryItems,
} from '../context/sales/SalesActions.jsx';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getLibrary: vi.fn(),
        createDirectSales: vi.fn(),
    },
}));

describe('SalesActions', () => {
    const dispatch = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
    });

    describe('action creators', () => {
        it('getLibraryItemsSuccess returns correct action', () => {
            const payload = { some: 'data' };

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
            const payload = { id: 'sale-1' };

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
            const result = {
                _embedded: {
                    libraryItemSummaryDTOList: [
                        { itemId: '1', title: 'Dune' },
                    ],
                },
            };

            apiClient.getLibrary.mockResolvedValue(result);

            await getMyLibraryItems(dispatch);

            expect(apiClient.getLibrary).toHaveBeenCalledTimes(1);
            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_ITEMS_SUCCESS,
                payload: result,
            });
        });

        it('dispatches error action when api call fails with plain error', async () => {
            apiClient.getLibrary.mockRejectedValue(new Error('Network error'));

            await getMyLibraryItems(dispatch);

            expect(apiClient.getLibrary).toHaveBeenCalledTimes(1);
            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_ITEMS_ERROR,
                payload: 'Network error',
            });
        });

        it('dispatches parsed error message when api call fails with JSON error message', async () => {
            apiClient.getLibrary.mockRejectedValue(
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
        it('dispatches success action and returns true when api call succeeds', async () => {
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            const result = { id: 'sale-123' };

            apiClient.createDirectSales.mockResolvedValue(result);

            const success = await createDirectSale(dispatch, body);

            expect(apiClient.createDirectSales).toHaveBeenCalledTimes(1);
            expect(apiClient.createDirectSales).toHaveBeenCalledWith(body);
            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_SUCCESS,
                payload: result,
            });
            expect(success).toBe(true);
        });

        it('dispatches error action and returns false when api call fails with plain error', async () => {
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            apiClient.createDirectSales.mockRejectedValue(new Error('Create sale failed'));

            const success = await createDirectSale(dispatch, body);

            expect(apiClient.createDirectSales).toHaveBeenCalledWith(body);
            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: 'Create sale failed',
            });
            expect(success).toBe(false);
        });

        it('dispatches parsed error action and returns false when api call fails with JSON error', async () => {
            const body = {
                itemsId: ['1'],
                priceValue: 20,
                priceCurrency: 'EUR',
            };

            apiClient.createDirectSales.mockRejectedValue(
                new Error(JSON.stringify({ message: 'Direct sale creation failed' }))
            );

            const success = await createDirectSale(dispatch, body);

            expect(dispatch).toHaveBeenCalledWith({
                type: CREATE_DIRECT_SALE_ERROR,
                payload: 'Direct sale creation failed',
            });
            expect(success).toBe(false);
        });
    });
});