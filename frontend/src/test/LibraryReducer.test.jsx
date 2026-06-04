import { describe, it, expect } from 'vitest';
import { libraryReducer, initialState } from '../context/library/LibraryReducer';
import { BASE_URL } from '../services/apiClient';
import {
    FETCH_DETAIL_ERROR, FETCH_DETAIL_SUCCESS,
    FETCH_LIBRARY_ERROR,
    FETCH_LIBRARY_SUCCESS,
    GET_LIBRARY_OPTIONS_SUCCESS,
    GET_LIBRARY_OPTIONS_ERROR,
    ADD_ITEM_SUCCESS,
    ADD_ITEM_ERROR
} from '../context/library/LibraryActions';

describe('libraryReducer', () => {

    it('stores library and addItem hrefs on GET_LIBRARY_OPTIONS_SUCCESS', () => {
        const action = {
            type: GET_LIBRARY_OPTIONS_SUCCESS,
            payload: {
                library: { href: `${BASE_URL}/my-library/` },
                'library-add': { href: `${BASE_URL}/my-library/add` }
            }
        };

        const result = libraryReducer(initialState, action);

        expect(result.libraryHref).toBe(`${BASE_URL}/my-library/`);
        expect(result.addItemHref).toBe(`${BASE_URL}/my-library/add`);
    });

    it('stores library items on FETCH_LIBRARY_SUCCESS', () => {
        const items = [
            {
                itemId: 'ITM-001',
                title: 'Dune',
                picture: null,
                _links: {
                    self: { href: `${BASE_URL}/my-library/ITM-001` }
                }
            }
        ];

        const action = {
            type: FETCH_LIBRARY_SUCCESS,
            payload: items
        };

        const result = libraryReducer(initialState, action);

        expect(result.items).toEqual([
            {
                itemId: 'ITM-001',
                title: 'Dune',
                picture: null,
                links: [
                    {
                        rel: 'self',
                        href: `${BASE_URL}/my-library/ITM-001`
                    }
                ]
            }
        ]);
    });

    it('stores error message on FETCH_LIBRARY_ERROR', () => {
        const action = {
            type: FETCH_LIBRARY_ERROR,
            payload: 'Failed to load library'
        };

        const result = libraryReducer(initialState, action);

        expect(result.error).toBe('Failed to load library');
    });

    it('stores item details on FETCH_DETAIL_SUCCESS', () => {
        const detail = {
            publicationType: 'BOOK',
            authorName: 'Frank Herbert',
            identifier: '9780441172719'
        };

        const action = {
            type: FETCH_DETAIL_SUCCESS,
            payload: {
                itemId: 'ITM-001',
                detail
            }
        };

        const result = libraryReducer(initialState, action);

        expect(result.details['ITM-001']).toEqual({
            publicationType: 'Book',
            authorName: 'Frank Herbert',
            identifier: '9780441172719'
        });
    });

    it('preserves existing details when adding a new detail', () => {
        const state = {
            ...initialState,
            details: {
                'ITM-001': {
                    publicationType: 'Book',
                    authorName: 'Frank Herbert',
                    identifier: '9780441172719'
                }
            }
        };

        const newDetail = {
            publicationType: 'MAGAZINE',
            authorName: 'Isaac Asimov',
            identifier: '9780553293357'
        };

        const action = {
            type: FETCH_DETAIL_SUCCESS,
            payload: {
                itemId: 'ITM-002',
                detail: newDetail
            }
        };

        const result = libraryReducer(state, action);

        expect(result.details).toEqual({
            'ITM-001': state.details['ITM-001'],
            'ITM-002': {
                publicationType: 'Magazine',
                authorName: 'Isaac Asimov',
                identifier: '9780553293357'
            }
        });
    });

    it('stores error message on FETCH_DETAIL_ERROR', () => {
        const action = {
            type: FETCH_DETAIL_ERROR,
            payload: 'Failed to load details'
        };

        const result = libraryReducer(initialState, action);

        expect(result.error).toBe('Failed to load details');
    });

    it('adds mapped item on ADD_ITEM_SUCCESS', () => {
        const state = {
            ...initialState,
            items: [
                {
                    itemId: 'ITM-001',
                    title: 'Dune',
                    picture: null,
                    links: []
                }
            ]
        };

        const action = {
            type: ADD_ITEM_SUCCESS,
            payload: {
                itemId: 'ITM-002',
                title: 'Foundation',
                picture: 'cover.jpg',
                _links: {
                    self: { href: `${BASE_URL}/my-library/ITM-002` }
                }
            }
        };

        const result = libraryReducer(state, action);

        expect(result.loading).toBe(false);
        expect(result.items).toEqual([
            state.items[0],
            {
                itemId: 'ITM-002',
                title: 'Foundation',
                picture: 'cover.jpg',
                links: [
                    {
                        rel: 'self',
                        href: `${BASE_URL}/my-library/ITM-002`
                    }
                ]
            }
        ]);
    });

    it('stores error and stops loading on ADD_ITEM_ERROR', () => {
        const action = {
            type: ADD_ITEM_ERROR,
            payload: 'Failed to add item'
        };

        const result = libraryReducer(initialState, action);

        expect(result.loading).toBe(false);
        expect(result.error).toBe('Failed to add item');
    });

    it('stores error message on GET_LIBRARY_OPTIONS_ERROR', () => {
        const action = {
            type: GET_LIBRARY_OPTIONS_ERROR,
            payload: 'Failed to load library options'
        };

        const result = libraryReducer(initialState, action);

        expect(result.error).toBe('Failed to load library options');
    });

    it('maps empty links when library item has no _links', () => {
        const action = {
            type: FETCH_LIBRARY_SUCCESS,
            payload: [
                {
                    itemId: 'ITM-003',
                    title: 'No Links Book',
                    picture: undefined
                }
            ]
        };

        const result = libraryReducer(initialState, action);

        expect(result.items).toEqual([
            {
                itemId: 'ITM-003',
                title: 'No Links Book',
                picture: null,
                links: []
            }
        ]);
    });

    it('returns current state for unknown action', () => {
        const action = {
            type: 'UNKNOWN_ACTION'
        };

        const result = libraryReducer(initialState, action);

        expect(result).toEqual(initialState);
    });

});