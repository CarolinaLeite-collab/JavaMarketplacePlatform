import {describe, expect, it} from 'vitest';
import {initialListsState, listsReducer} from '../../context/lists/ListsReducer.jsx';

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

    describe('GET_LISTS_SUCCESS', () => {
        it('maps list payload from embedded shape', () => {
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

        it('defaults to empty array when payload is null', () => {
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload: null });
            expect(result.lists).toEqual([]);
            expect(result.error).toBeNull();
        });

        it('maps private:true to isPrivate:true', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [aList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].isPrivate).toBe(true);
        });

        it('maps private:false to isPrivate:false', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [anotherList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].isPrivate).toBe(false);
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

        it('maps _links into a rel/href array', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [aList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].links).toEqual([
                { rel: 'add-item', href: 'http://localhost:8081/my-lists/LIST-001' },
                { rel: 'delete', href: 'http://localhost:8081/my-lists/LIST-001' },
            ]);
        });

        it('formats genreId into a display genre', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [aList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_LISTS_SUCCESS', payload });
            expect(result.lists[0].genre).toBe('Fiction');
        });
    });

    it('sets error on GET_LISTS_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'GET_LISTS_ERROR', payload: 'network error' });
        expect(result.error).toBe('network error');
    });

    describe('CREATE_LIST_SUCCESS', () => {
        it('appends the new mapped list to state', () => {
            const stateWithOne = { ...initialListsState, lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }] };
            const result = listsReducer(stateWithOne, { type: 'CREATE_LIST_SUCCESS', payload: anotherList });
            expect(result.lists).toHaveLength(2);
            expect(result.lists[1].listId).toBe('LIST-002');
            expect(result.lists[1].isPrivate).toBe(false);
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
            lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }],
        };

        it('updates the matching list on MAKE_LIST_PUBLIC_SUCCESS', () => {
            const updated = { ...aList, private: false };
            const result = listsReducer(stateWithList, { type: 'MAKE_LIST_PUBLIC_SUCCESS', payload: updated });
            expect(result.lists[0].isPrivate).toBe(false);
        });

        it('updates the matching list on MAKE_LIST_PRIVATE_SUCCESS', () => {
            const statePublic = { ...initialListsState, lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: false, sharedUntil: null, links: [], itemIds: [] }] };
            const result = listsReducer(statePublic, { type: 'MAKE_LIST_PRIVATE_SUCCESS', payload: aList });
            expect(result.lists[0].isPrivate).toBe(true);
        });

        it('leaves other lists untouched', () => {
            const stateWithTwo = {
                ...initialListsState,
                lists: [
                    { listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] },
                    { listId: 'LIST-002', name: 'Sci-Fi', genre: 'Sci Fi', isPrivate: true, sharedUntil: null, links: [], itemIds: [] },
                ],
            };
            const updated = { ...aList, private: false };
            const result = listsReducer(stateWithTwo, { type: 'MAKE_LIST_PUBLIC_SUCCESS', payload: updated });
            expect(result.lists[1].listId).toBe('LIST-002');
            expect(result.lists[1].isPrivate).toBe(true);
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
        it('removes the list matching the deleted href', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }],
                error: 'previous error',
            };
            const result = listsReducer(stateWithList, { type: 'DELETE_LIST_SUCCESS', payload: 'LIST-001' });
            expect(result.error).toBeNull();
            expect(result.lists).toHaveLength(0);
        });

        it('leaves non-matching lists in place', () => {
            const stateWithTwo = {
                ...initialListsState,
                lists: [
                    { listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] },
                    { listId: 'LIST-002', name: 'Sci-Fi', genre: 'Sci Fi', isPrivate: false, sharedUntil: null, links: [], itemIds: [] },
                ],
            };
            const result = listsReducer(stateWithTwo, { type: 'DELETE_LIST_SUCCESS', payload: 'LIST-001' });
            expect(result.lists).toHaveLength(1);
            expect(result.lists[0].listId).toBe('LIST-002');
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
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }],
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
                    { listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] },
                    { listId: 'LIST-002', name: 'Sci-Fi', genre: 'Sci Fi', isPrivate: true, sharedUntil: null, links: [], itemIds: [] },
                ],
            };
            const result = listsReducer(stateWithTwo, { type: 'ADD_ITEM_TO_LIST_SUCCESS', payload: aList });
            expect(result.lists[1].listId).toBe('LIST-002');
            expect(result.lists[1].name).toBe('Sci-Fi');
        });

        it('maps itemsId to itemIds on success', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }],
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

    describe('REMOVE_ITEM_FROM_LIST_SUCCESS', () => {
        it('updates the matching list in state', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: ['ITEM-001'] }],
            };
            const updated = { ...aList, itemsId: [] };
            const result = listsReducer(stateWithList, { type: 'REMOVE_ITEM_FROM_LIST_SUCCESS', payload: updated });
            expect(result.lists[0].itemIds).toEqual([]);
            expect(result.error).toBeNull();
        });
    });

    it('sets error on REMOVE_ITEM_FROM_LIST_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'REMOVE_ITEM_FROM_LIST_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });

    describe('GET_PUBLIC_LISTS_SUCCESS', () => {
        it('maps embedded payload into publicLists', () => {
            const payload = { _embedded: { listOfItemsResponseDTOList: [anotherList] }, _links: {} };
            const result = listsReducer(initialListsState, { type: 'GET_PUBLIC_LISTS_SUCCESS', payload });
            expect(result.publicLists).toHaveLength(1);
            expect(result.publicLists[0].listId).toBe('LIST-002');
        });

        it('handles flat array payload', () => {
            const result = listsReducer(initialListsState, { type: 'GET_PUBLIC_LISTS_SUCCESS', payload: [anotherList] });
            expect(result.publicLists).toHaveLength(1);
        });

        it('does not affect the private lists array', () => {
            const stateWithList = {
                ...initialListsState,
                lists: [{ listId: 'LIST-001', name: 'My Fiction', genre: 'Fiction', isPrivate: true, sharedUntil: null, links: [], itemIds: [] }],
            };
            const result = listsReducer(stateWithList, { type: 'GET_PUBLIC_LISTS_SUCCESS', payload: [anotherList] });
            expect(result.lists).toHaveLength(1);
            expect(result.publicLists).toHaveLength(1);
        });
    });

    it('sets error on GET_PUBLIC_LISTS_ERROR', () => {
        const result = listsReducer(initialListsState, { type: 'GET_PUBLIC_LISTS_ERROR', payload: 'failed' });
        expect(result.error).toBe('failed');
    });
});

