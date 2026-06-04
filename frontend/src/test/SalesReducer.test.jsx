import {describe, expect, it} from 'vitest';
import {initialSalesState, salesReducer} from '../context/sales/SalesReducer';
import {
    CLEAR_SALES_MESSAGES,
    CREATE_DIRECT_SALE_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    GET_LIBRARY_ITEMS_ERROR,
    GET_LIBRARY_ITEMS_SUCCESS,
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

    it('maps library items and keeps only sellable items on GET_LIBRARY_ITEMS_SUCCESS', () => {
        const state = {
            ...initialSalesState,
            error: 'Old error',
        };

        const action = {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: [
                {
                    itemId: 'item-1',
                    title: 'Dune',
                    picture: 'dune.jpg',
                    saleStatus: 'NotOnSale',
                    links: [
                        { rel: 'self', href: '/items/item-1' },
                        { rel: 'create-direct-sale', href: '/direct-sales' },
                    ],
                },
                {
                    itemId: 'item-2',
                    title: '1984',
                    picture: '1984.jpg',
                    saleStatus: 'OnDirectSale',
                    links: [
                        { rel: 'self', href: '/items/item-2' },
                    ],
                },
            ],
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
                    saleStatus: 'NotOnSale',
                    selfHref: '/items/item-1',
                    createDirectSaleHref: '/direct-sales',
                    links: [
                        { rel: 'self', href: '/items/item-1' },
                        { rel: 'create-direct-sale', href: '/direct-sales' },
                    ],
                },
            ],
        });
    });

    it('returns an empty array when GET_LIBRARY_ITEMS_SUCCESS payload is empty', () => {
        const state = {
            ...initialSalesState,
            libraryItems: [
                {
                    value: 'old',
                    label: 'Old item',
                    picture: 'old.jpg',
                    saleStatus: 'NotOnSale',
                    selfHref: '/items/old',
                    createDirectSaleHref: '/direct-sales',
                    links: [],
                },
            ],
            error: 'Old error',
        };

        const result = salesReducer(state, {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: [],
        });

        expect(result).toEqual({
            ...state,
            error: null,
            libraryItems: [],
        });
    });

    it('returns an empty array when GET_LIBRARY_ITEMS_SUCCESS payload is missing', () => {
        const state = {
            ...initialSalesState,
            libraryItems: [
                {
                    value: 'old',
                    label: 'Old item',
                    picture: 'old.jpg',
                    saleStatus: 'NotOnSale',
                    selfHref: '/items/old',
                    createDirectSaleHref: '/direct-sales',
                    links: [],
                },
            ],
            error: 'Old error',
        };

        const result = salesReducer(state, {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: undefined,
        });

        expect(result).toEqual({
            ...state,
            error: null,
            libraryItems: [],
        });
    });

    it('filters out items without create-direct-sale link', () => {
        const result = salesReducer(initialSalesState, {
            type: GET_LIBRARY_ITEMS_SUCCESS,
            payload: [
                {
                    itemId: 'item-1',
                    title: 'Dune',
                    picture: 'dune.jpg',
                    saleStatus: 'OnDirectSale',
                    links: [{ rel: 'self', href: '/items/item-1' }],
                },
                {
                    itemId: 'item-2',
                    title: '1984',
                    picture: '1984.jpg',
                    saleStatus: 'Sold',
                    links: [{ rel: 'self', href: '/items/item-2' }],
                },
            ],
        });

        expect(result).toEqual({
            ...initialSalesState,
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

    it('sets success message and clears error on CREATE_DIRECT_SALE_SUCCESS', () => {
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
            successMessage: 'The item was successfully put on direct sale.',
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