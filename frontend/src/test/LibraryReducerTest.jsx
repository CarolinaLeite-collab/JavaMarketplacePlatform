import { describe, it, expect } from 'vitest';
import { libraryReducer, initialState } from '../context/library/LibraryReducer';

describe('libraryReducer', () => {

    it('sets loading to true and clears error on LOADING', () => {
        const state = {
            ...initialState,
            error: 'Some error'
        };

        const action = { type: 'LOADING' };

        const result = libraryReducer(state, action);

        expect(result.loading).toBe(true);
        expect(result.error).toBeNull();
    });

    it('stores library items on FETCH_LIBRARY_SUCCESS', () => {
        const items = [
            {
                itemId: 'ITM-001',
                title: 'Dune',
                picture: null,
                _links: {
                    self: { href: 'http://localhost:8081/my-library/ITM-001' }
                }
            }
        ];

        const action = {
            type: 'FETCH_LIBRARY_SUCCESS',
            payload: items
        };

        const result = libraryReducer(initialState, action);

        expect(result.loading).toBe(false);

        expect(result.items).toEqual([
            {
                itemId: 'ITM-001',
                title: 'Dune',
                picture: null,
                links: [
                    { rel: 'self', href: 'http://localhost:8081/my-library/ITM-001' }
                ]
            }
        ]);
    });

    it('stores error message on FETCH_LIBRARY_ERROR', () => {
        const action = {
            type: 'FETCH_LIBRARY_ERROR',
            payload: 'Failed to load library'
        };

        const result = libraryReducer(initialState, action);

        expect(result.loading).toBe(false);
        expect(result.error).toBe('Failed to load library');
    });

    it('stores item details on FETCH_DETAIL_SUCCESS', () => {
        const detail = {
            publicationType: 'Book',
            authorName: 'Frank Herbert',
            identifier: '9780441172719'
        };

        const action = {
            type: 'FETCH_DETAIL_SUCCESS',
            payload: {
                itemId: 'ITM-001',
                detail
            }
        };

        const result = libraryReducer(initialState, action);

        expect(result.details).toEqual({
            'ITM-001': detail
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
            publicationType: 'Book',
            authorName: 'Isaac Asimov',
            identifier: '9780553293357'
        };

        const action = {
            type: 'FETCH_DETAIL_SUCCESS',
            payload: {
                itemId: 'ITM-002',
                detail: newDetail
            }
        };

        const result = libraryReducer(state, action);

        expect(result.details).toEqual({
            'ITM-001': state.details['ITM-001'],
            'ITM-002': newDetail
        });
    });

    it('stores error message on FETCH_DETAIL_ERROR', () => {
        const action = {
            type: 'FETCH_DETAIL_ERROR',
            payload: 'Failed to load details'
        };

        const result = libraryReducer(initialState, action);

        expect(result.error).toBe('Failed to load details');
    });

    it('returns current state for unknown action', () => {
        const action = {
            type: 'UNKNOWN_ACTION'
        };

        const result = libraryReducer(initialState, action);

        expect(result).toEqual(initialState);
    });

});