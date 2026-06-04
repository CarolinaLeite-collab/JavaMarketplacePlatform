import {describe, expect, it} from 'vitest';
import {initialListsState, listsReducer} from '../context/lists/ListsReducer';

const aList = {
    listId: 'LIST-001',
    name: 'My Fiction',
    genreId: 'fiction',
    private: true,
    sharedUntil: null,
    _links: {
        'add-item': { href: 'http://localhost:8081/my-lists/LIST-001' },
        delete: { href: 'http://localhost:8081/my-lists/LIST-001' },
    },
};

const anotherList = {
    listId: 'LIST-002',
    name: 'Sci-Fi',
    genreId: 'sci-fi',
    private: false,
    sharedUntil: null,
    _links: {},
};

describe('listsReducer', () => {

    it('returns initial state for unknown action', () => {
        const result = listsReducer(initialListsState, { type: 'UNKNOWN' });
        expect(result).toEqual(initialListsState);
    });

    describe('GET_LIST_OPTIONS_SUCCESS', () => {
        it('extracts createListHref and myListsHref from links', () => {
            const payload = {
                'create-list': { href: 'http://localhost:8081/my-lists/' },
                collection: { href: 'http://localhost:8081/my-lists/' },
            };
            const result = listsReducer(initialListsState, { type: 'GET_LIST_OPTIONS_SUCCESS', payload });
            expect(result.createListHref).toBe('http://localhost:8081/my-lists/');
            expect(result.myListsHref).toBe('http://localhost:8081/my-lists/');
        });

        it('sets null when links are missing', () => {
            const result = listsReducer(initialListsState, { type: 'GET_LIST_OPTIONS_SUCCESS', payload: {} });
            expect(result.createListHref).toBeNull();
            expect(result.myListsHref).toBeNull();
        });
    });

    it('sets error on GET_LIST_OPTIONS_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'GET_LIST_OPTIONS_ERROR', payload: 'forbidden' });
        expect(result.error).toBe('forbidden');
    });

    describe('GET_LISTS_SUCCESS', () => {
        it('maps list payload and extracts createListHref', () => {
            const payload = {
                _embedded: { listOfItemsResponseDTOList: [aList] },
                _links: { 'create-list': { href: 'http://localhost:8081/my-lists/' } },
            };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists).toHaveLength(1);
            expect(result.lists[0].listId).toBe('LIST-001');
            expect(result.error).toBeNull();
        });

        it('handles flat array payload', () => {
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload: [aList] });
            expect(result.lists).toHaveLength(1);
        });

        it('defaults to empty array when embedded is missing', () => {
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload: {} });
            expect(result.lists).toEqual([]);
        });

        it('maps itemsId to itemIds', () => {
            const listWithItems = { ...aList, itemsId: ['ITEM-001', 'ITEM-002'] };
            const payload = { _embedded: { listOfItemsResponseDTOList: [listWithItems] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].itemIds).toEqual(['ITEM-001', 'ITEM-002']);
        });

        it('defaults itemIds to empty array when itemsId is absent', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [aList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].itemIds).toEqual([]);
        });
    });

    it('sets error on GET_LISTS_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'GET_LISTS_ERROR', payload: 'network error' });
        expect(result.error).toBe('network error');
    });

    describe('CREATE_LIST_SUCCESS', () => {
        it('appends the new list to state', () => {
            const stateWithOne = { ...initialListsState, lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [] }] };
            const result = listsReducer(stateWithOne, { type: 'CREATE_LIST_SUCCESS', payload: anotherList });
            expect(result.lists).toHaveLength(2);
            expect(result.lists[1].listId).toBe('LIST-002');
        });
    });

    it('sets error on CREATE_LIST_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'CREATE_LIST_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    describe('GET_GENRES_SUCCESS', () => {
        it('maps genres to value/label pairs', () => {
            const genres = [{ genreId: 'fiction', genreName: 'Fiction' }];
            const result = listsReducer(initialListsState, { type: 'GET_GENRES_SUCCESS', payload: genres });
            expect(result.genres).toEqual([{ value: 'fiction', label: 'Fiction' }]);
        });
    });

    it('sets error on GET_GENRES_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'GET_GENRES_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    describe('MAKE_LIST_PUBLIC_SUCCESS / MAKE_LIST_PRIVATE_SUCCESS', () => {
        const stateWithList = {
            ...initialListsState,
            lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [] }],
        };

        it('updates the matching list on MAKE_LIST_PUBLIC_SUCCESS', () => {
            const updated = { ...aList, private: false };
            const result = listsReducer(stateWithList, { type: 'MAKE_LIST_PUBLIC_SUCCESS', payload: updated });
            expect(result.lists[0].visibility).toBe('public');
        });

        it('updates the matching list on MAKE_LIST_PRIVATE_SUCCESS', () => {
            const statePublic = { ...initialListsState, lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'public', sharedUntil: null, links: [] }] };
            const result = listsReducer(statePublic, { type: 'MAKE_LIST_PRIVATE_SUCCESS', payload: aList });
            expect(result.lists[0].visibility).toBe('private');
        });

        it('leaves other lists untouched', () => {
            const stateWithTwo = {
                ...initialListsState,
                lists: [
                    { listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [] },
                    { listId: 'LIST-002', name: 'Sci-Fi', genre: 'Sci Fi', visibility: 'private', sharedUntil: null, links: [] },
                ],
            };
            const updated = { ...aList, private: false };
            const result = listsReducer(stateWithTwo, { type: 'MAKE_LIST_PUBLIC_SUCCESS', payload: updated });
            expect(result.lists[1].listId).toBe('LIST-002');
            expect(result.lists[1].visibility).toBe('private');
        });
    });

    it('sets error on MAKE_LIST_PUBLIC_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'MAKE_LIST_PUBLIC_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    it('sets error on MAKE_LIST_PRIVATE_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'MAKE_LIST_PRIVATE_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    describe('DELETE_LIST_SUCCESS', () => {
        it('clears the error; list removal happens via the subsequent GET_LISTS_SUCCESS refresh', () => {
            const stateWithError = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [] }],
                error: 'previous error',
            };
            const result = listsReducer(stateWithError, { type: 'DELETE_LIST_SUCCESS', payload: 'http://localhost:8081/my-lists/LIST-001' });
            expect(result.error).toBeNull();
        });
    });

    it('sets error on DELETE_LIST_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'DELETE_LIST_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    describe('ADD_ITEM_TO_LIST_SUCCESS', () => {
        it('updates the matching list in state', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [] }],
            };
            const updated = { ...aList, name: 'My Fiction Updated' };
            const result = listsReducer(stateWithList, { type: 'ADD_ITEM_TO_LIST_SUCCESS', payload: updated });
            expect(result.lists[0].name).toBe('My Fiction Updated');
            expect(result.error).toBeNull();
        });

        it('leaves other lists untouched', () => {
            const stateWithTwo = {
                ...initialListsState,
                lists: [
                    { listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [], itemIds: [] },
                    { listId: 'LIST-002', name: 'Sci-Fi', genre: 'Sci Fi', visibility: 'private', sharedUntil: null, links: [], itemIds: [] },
                ],
            };
            const result = listsReducer(stateWithTwo, { type: 'ADD_ITEM_TO_LIST_SUCCESS', payload: aList });
            expect(result.lists[1].listId).toBe('LIST-002');
            expect(result.lists[1].name).toBe('Sci-Fi');
        });

        it('maps itemsId to itemIds on success', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', visibility: 'private', sharedUntil: null, links: [], itemIds: [] }],
            };
            const updated = { ...aList, itemsId: ['ITEM-001', 'ITEM-002'] };
            const result = listsReducer(stateWithList, { type: 'ADD_ITEM_TO_LIST_SUCCESS', payload: updated });
            expect(result.lists[0].itemIds).toEqual(['ITEM-001', 'ITEM-002']);
        });
    });

    it('sets error on ADD_ITEM_TO_LIST_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'ADD_ITEM_TO_LIST_ERROR', payload: 'item not in library' });
        expect(result.error).toBe('item not in library');
    });
});
