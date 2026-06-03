import { describe, it, expect } from 'vitest';
import { salesReducer, initialSalesState } from '../context/sales/SalesReducer';
import {
    GET_LIBRARY_ITEMS_SUCCESS,
    GET_LIBRARY_ITEMS_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    CREATE_DIRECT_SALE_ERROR,
    CLEAR_SALES_MESSAGES,
} from '../context/sales/SalesActions.jsx';

describe('salesReducer', () => {
    it('returns the current state for an unknown action', () => {
        const state = {
            ...initialSalesState,
            error: 'Something went wrong',
        };

        const result = salesReducer(state, { type: 'UNKNOWN_ACTION' });

        expect(result).toBe(state);
    });

    it('maps library items on GET_LIBRARY_ITEMS_SUCCESS', () => {
        const state = {
            ...initialSalesState,
            error: 'Old error',
        };

        const action = {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: {
                _embedded: {
                    libraryItemSummaryDTOList: [
                        {
                            itemId: 'item-1',
                            title: 'Dune',
                            picture: 'dune.jpg',
                            _links: {
                                self: { href: '/api/library/items/item-1' },
                            },
                        },
                        {
                            itemId: 'item-2',
                            title: '1984',
                            picture: '1984.jpg',
                        },
                    ],
                },
            },
        };

        const result = salesReducer(state, action);

        expect(result).toEqual({
            ...state,
            error: null,
            libraryItems: [
                {
                    value: 'item-1',
                    label: 'Dune',
                    picture: 'dune.jpg',
                    href: '/api/library/items/item-1',
                },
                {
                    value: 'item-2',
                    label: '1984',
                    picture: '1984.jpg',
                    href: null,
                },
            ],
        });
    });

    it('uses an empty array when GET_LIBRARY_ITEMS_SUCCESS payload has no embedded items', () => {
        const state = {
            ...initialSalesState,
            libraryItems: [{ value: 'old', label: 'Old', picture: 'old.jpg', href: '/old' }],
            error: 'Old error',
        };

        const result = salesReducer(state, {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: {},
        });

        expect(result).toEqual({
            ...state,
            error: null,
            libraryItems: [],
        });
    });

    it('sets error on GET_LIBRARY_ITEMS_ERROR', () => {
        const result = salesReducer(initialSalesState, {
            type: GET_LIBRARY_ITEMS_ERROR,
            payload: 'Failed to load library items',
        });

        expect(result).toEqual({
            ...initialSalesState,
            error: 'Failed to load library items',
        });
    });

    it('clears error on CREATE_DIRECT_SALE_SUCCESS', () => {
        const state = {
            ...initialSalesState,
            error: 'Previous error',
            successMessage: null,
        };

        const result = salesReducer(state, {
            type: CREATE_DIRECT_SALE_SUCCESS,
        });

        expect(result).toEqual({
            ...state,
            error: null,
        });
    });

    it('sets error on CREATE_DIRECT_SALE_ERROR', () => {
        const result = salesReducer(initialSalesState, {
            type: CREATE_DIRECT_SALE_ERROR,
            payload: 'Failed to create direct sale',
        });

        expect(result).toEqual({
            ...initialSalesState,
            error: 'Failed to create direct sale',
        });
    });

    it('clears error and successMessage on CLEAR_SALES_MESSAGES', () => {
        const state = {
            ...initialSalesState,
            error: 'Some error',
            successMessage: 'Created successfully',
        };

        const result = salesReducer(state, {
            type: CLEAR_SALES_MESSAGES,
        });

        expect(result).toEqual({
            ...state,
            error: null,
            successMessage: null,
        });
    });
});