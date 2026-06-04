import { describe, it, expect, vi, beforeEach } from 'vitest';

import {
    getLibrary,
    getLibraryDetail,
    FETCH_LIBRARY_SUCCESS,
    FETCH_LIBRARY_ERROR,
    FETCH_DETAIL_SUCCESS,
    FETCH_DETAIL_ERROR, GET_LIBRARY_OPTIONS_ERROR, GET_LIBRARY_OPTIONS_SUCCESS, getLibraryOptions,
} from '../context/library/LibraryActions';

import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        getLibraryOptions: vi.fn(),
    },
}));

describe('LibraryActions', () => {
    let dispatch;

    beforeEach(() => {
        dispatch = vi.fn();
        vi.clearAllMocks();
    });

    describe('getLibraryOptions', () => {
        it('dispatches GET_LIBRARY_OPTIONS_SUCCESS', async () => {
            const links = {
                self: {
                    href: 'http://localhost:8081/my-library'
                }
            };

            apiClient.getLibraryOptions.mockResolvedValue({
                _links: links
            });

            await getLibraryOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_OPTIONS_SUCCESS,
                payload: links,
            });
        });

        it('dispatches GET_LIBRARY_OPTIONS_ERROR on failure', async () => {
            apiClient.getLibraryOptions.mockRejectedValue(
                new Error('network error')
            );

            await getLibraryOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: GET_LIBRARY_OPTIONS_ERROR,
                payload: 'Error: network error',
            });
        });
    });

    describe('getLibrary', () => {
        it('dispatches FETCH_LIBRARY_SUCCESS with library items', async () => {
            const items = [
                { itemId: 'ITEM-001', title: 'Book' }
            ];

            const result = {
                _embedded: {
                    anyKey: items
                }
            };

            apiClient.getByHref.mockResolvedValue(result);

            await getLibrary(dispatch, 'http://fake-url');

            expect(apiClient.getByHref).toHaveBeenCalledWith('http://fake-url');

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_SUCCESS,
                payload: items,
            });
        });

        it('dispatches FETCH_LIBRARY_SUCCESS with empty array when response has no embedded items', async () => {
            apiClient.getByHref.mockResolvedValue({});

            await getLibrary(dispatch, 'http://fake-url');

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_SUCCESS,
                payload: [],
            });
        });

        it('dispatches FETCH_LIBRARY_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(
                new Error('network error')
            );

            await getLibrary(dispatch, 'http://fake-url');

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_ERROR,
                payload: 'Error: network error',
            });
        });

        it('dispatches FETCH_LIBRARY_ERROR when href is missing', async () => {
            await getLibrary(dispatch, null);

            expect(apiClient.getByHref).not.toHaveBeenCalled();

            expect(dispatch).toHaveBeenCalledWith({
                type: FETCH_LIBRARY_ERROR,
                payload: 'Missing library href',
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