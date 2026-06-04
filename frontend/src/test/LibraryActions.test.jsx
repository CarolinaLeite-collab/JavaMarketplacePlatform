import { describe, it, expect, vi, beforeEach } from 'vitest';

import {
    getLibrary,
    getLibraryDetail,
    FETCH_LIBRARY_SUCCESS,
    FETCH_LIBRARY_ERROR,
    FETCH_DETAIL_SUCCESS,
    FETCH_DETAIL_ERROR,
} from '../context/library/LibraryActions';

import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getLibrary: vi.fn(),
        getByHref: vi.fn(),
    },
}));

describe('LibraryActions', () => {
    let dispatch;

    beforeEach(() => {
        dispatch = vi.fn();
        vi.clearAllMocks();
    });

    describe('getLibrary', () => {
        it('dispatches FETCH_LIBRARY_SUCCESS with library items', async () => {
            const result = {
                _embedded: {
                    libraryItemSummaryDTOList: [
                        {
                            itemId: 'ITEM-001',
                            title: 'Book'
                        }
                    ]
                }
            };

            apiClient.getLibrary.mockResolvedValue(result);

            await getLibrary(dispatch);

            expect(apiClient.getLibrary).toHaveBeenCalled();

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_SUCCESS,
                payload: result._embedded.libraryItemSummaryDTOList,
            });
        });

        it('dispatches FETCH_LIBRARY_ERROR on failure', async () => {
            apiClient.getLibrary.mockRejectedValue(
                new Error('network error')
            );

            await getLibrary(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_ERROR,
                payload: 'Error: network error',
            });
        });
    });

    describe('getLibraryDetail', () => {
        it('dispatches FETCH_DETAIL_SUCCESS with item detail', async () => {
            const detail = {
                itemId: 'ITEM-001',
                title: 'Dune',
                publicationType: 'BOOK',
            };

            apiClient.getByHref.mockResolvedValue(detail);

            await getLibraryDetail(
                dispatch,
                'http://localhost:8081/items/ITEM-001',
                'ITEM-001'
            );

            expect(apiClient.getByHref).toHaveBeenCalledWith(
                'http://localhost:8081/items/ITEM-001'
            );

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_DETAIL_SUCCESS,
                payload: {
                    itemId: 'ITEM-001',
                    detail,
                },
            });
        });

        it('dispatches FETCH_DETAIL_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(
                new Error('item not found')
            );

            await getLibraryDetail(
                dispatch,
                'http://localhost:8081/items/ITEM-001',
                'ITEM-001'
            );

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_DETAIL_ERROR,
                payload: 'Error: item not found',
            });
        });
    });
});