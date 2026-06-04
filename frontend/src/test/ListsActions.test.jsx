import { describe, it, expect, vi, beforeEach } from 'vitest';
import {
    createList, getMyLists, getGenres, getListsOptions,
    makeListPublic, makeListPrivate, deleteList, addItemToList,
    CREATE_LIST_SUCCESS, CREATE_LIST_ERROR,
    GET_LISTS_SUCCESS, GET_LISTS_ERROR,
    GET_GENRES_SUCCESS, GET_GENRES_ERROR,
    GET_LIST_OPTIONS_SUCCESS, GET_LIST_OPTIONS_ERROR,
    MAKE_LIST_PUBLIC_SUCCESS, MAKE_LIST_PUBLIC_ERROR,
    MAKE_LIST_PRIVATE_SUCCESS, MAKE_LIST_PRIVATE_ERROR,
    DELETE_LIST_SUCCESS, DELETE_LIST_ERROR,
    ADD_ITEM_TO_LIST_SUCCESS, ADD_ITEM_TO_LIST_ERROR,
} from '../context/lists/ListsActions';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getGenres: vi.fn(),
        getListsOptions: vi.fn(),
        getByHref: vi.fn(),
        postByHref: vi.fn(),
        patchByHref: vi.fn(),
        patchNoBodyByHref: vi.fn(),
        deleteByHref: vi.fn(),
    },
}));

describe('ListsActions', () => {
    let dispatch;

    beforeEach(() => {
        dispatch = vi.fn();
        vi.clearAllMocks();
    });

    describe('getListsOptions', () => {
        it('dispatches GET_LIST_OPTIONS_SUCCESS with _links on success', async () => {
            const links = { 'create-list': { href: 'http://localhost:8081/my-lists/' }, collection: { href: 'http://localhost:8081/my-lists/' } };
            apiClient.getListsOptions.mockResolvedValue({ _links: links });

            await getListsOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({ type: GET_LIST_OPTIONS_SUCCESS, payload: links });
        });

        it('dispatches GET_LIST_OPTIONS_ERROR on failure', async () => {
            apiClient.getListsOptions.mockRejectedValue(new Error('network error'));

            await getListsOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({ type: GET_LIST_OPTIONS_ERROR, payload: 'Error: network error' });
        });
    });

    describe('getMyLists', () => {
        it('dispatches GET_LISTS_SUCCESS with the result', async () => {
            const lists = { _embedded: { listOfItemsResponseDTOList: [] }, _links: {} };
            apiClient.getByHref.mockResolvedValue(lists);

            await getMyLists(dispatch, 'http://localhost:8081/my-lists/');

            expect(dispatch).toHaveBeenCalledWith({ type: GET_LISTS_SUCCESS, payload: lists });
        });

        it('dispatches GET_LISTS_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(new Error('network error'));

            await getMyLists(dispatch, 'http://localhost:8081/my-lists/');

            expect(dispatch).toHaveBeenCalledWith({ type: GET_LISTS_ERROR, payload: 'Error: network error' });
        });

        it('dispatches empty array when result is null', async () => {
            apiClient.getByHref.mockResolvedValue(null);

            await getMyLists(dispatch, 'http://localhost:8081/my-lists/');

            expect(dispatch).toHaveBeenCalledWith({ type: GET_LISTS_SUCCESS, payload: [] });
        });
    });

    describe('getGenres', () => {
        it('dispatches GET_GENRES_SUCCESS with genres', async () => {
            const genres = [{ genreId: 'fiction', genreName: 'Fiction' }];
            apiClient.getByHref.mockResolvedValue(genres);

            await getGenres(dispatch, '/genres');

            expect(dispatch).toHaveBeenCalledWith({ type: GET_GENRES_SUCCESS, payload: genres });
        });

        it('dispatches GET_GENRES_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(new Error('failed'));

            await getGenres(dispatch, '/genres');

            expect(dispatch).toHaveBeenCalledWith({ type: GET_GENRES_ERROR, payload: 'Error: failed' });
        });
    });

    describe('createList', () => {
        const myListsHref = 'http://localhost:8081/my-lists/';

        it('dispatches CREATE_LIST_SUCCESS and refreshes lists on success', async () => {
            const newList = { listId: 'LIST-001', name: 'My List' };
            const allLists = { _embedded: { listOfItemsResponseDTOList: [newList] }, _links: {} };
            apiClient.postByHref.mockResolvedValue(newList);
            apiClient.getByHref.mockResolvedValue(allLists);

            const result = await createList(dispatch, 'http://localhost:8081/my-lists/', { name: 'My List', genreId: 'fiction' }, myListsHref);

            expect(result).toBe(true);
            expect(dispatch).toHaveBeenCalledWith({ type: CREATE_LIST_SUCCESS, payload: newList });
        });

        it('dispatches CREATE_LIST_ERROR and returns false on failure', async () => {
            apiClient.postByHref.mockRejectedValue(new Error('bad request'));

            const result = await createList(dispatch, 'http://localhost:8081/my-lists/', {}, myListsHref);

            expect(result).toBe(false);
            expect(dispatch).toHaveBeenCalledWith({ type: CREATE_LIST_ERROR, payload: 'bad request' });
        });
    });

    describe('makeListPublic', () => {
        const links = [{ rel: 'make-public', href: 'http://localhost:8081/my-lists/LIST-001/visibility' }];

        it('dispatches MAKE_LIST_PUBLIC_SUCCESS on success', async () => {
            const updated = { listId: 'LIST-001', private: false };
            apiClient.patchByHref.mockResolvedValue(updated);

            await makeListPublic(dispatch, links, 30);

            expect(dispatch).toHaveBeenCalledWith({ type: MAKE_LIST_PUBLIC_SUCCESS, payload: updated });
        });

        it('dispatches MAKE_LIST_PUBLIC_ERROR on failure', async () => {
            apiClient.patchByHref.mockRejectedValue(new Error('failed'));

            await makeListPublic(dispatch, links, 30);

            expect(dispatch).toHaveBeenCalledWith({ type: MAKE_LIST_PUBLIC_ERROR, payload: 'Error: failed' });
        });

        it('does nothing when make-public link is missing', async () => {
            await makeListPublic(dispatch, [], 30);
            expect(dispatch).not.toHaveBeenCalled();
        });
    });

    describe('makeListPrivate', () => {
        const links = [{ rel: 'make-private', href: 'http://localhost:8081/my-lists/LIST-001/visibility' }];

        it('dispatches MAKE_LIST_PRIVATE_SUCCESS on success', async () => {
            const updated = { listId: 'LIST-001', private: true };
            apiClient.patchNoBodyByHref.mockResolvedValue(updated);

            await makeListPrivate(dispatch, links);

            expect(dispatch).toHaveBeenCalledWith({ type: MAKE_LIST_PRIVATE_SUCCESS, payload: updated });
        });

        it('dispatches MAKE_LIST_PRIVATE_ERROR on failure', async () => {
            apiClient.patchNoBodyByHref.mockRejectedValue(new Error('failed'));

            await makeListPrivate(dispatch, links);

            expect(dispatch).toHaveBeenCalledWith({ type: MAKE_LIST_PRIVATE_ERROR, payload: 'Error: failed' });
        });

        it('does nothing when make-private link is missing', async () => {
            await makeListPrivate(dispatch, []);
            expect(dispatch).not.toHaveBeenCalled();
        });
    });

    describe('deleteList', () => {
        const links = [{ rel: 'delete', href: 'http://localhost:8081/my-lists/LIST-001' }];
        const myListsHref = 'http://localhost:8081/my-lists/';

        it('dispatches DELETE_LIST_SUCCESS and refreshes lists on success', async () => {
            apiClient.deleteByHref.mockResolvedValue(null);
            apiClient.getByHref.mockResolvedValue({ _embedded: { listOfItemsResponseDTOList: [] }, _links: {} });

            await deleteList(dispatch, links, myListsHref);

            expect(dispatch).toHaveBeenCalledWith({ type: DELETE_LIST_SUCCESS, payload: 'http://localhost:8081/my-lists/LIST-001' });
        });

        it('dispatches DELETE_LIST_ERROR on failure', async () => {
            apiClient.deleteByHref.mockRejectedValue(new Error('failed'));

            await deleteList(dispatch, links, myListsHref);

            expect(dispatch).toHaveBeenCalledWith({ type: DELETE_LIST_ERROR, payload: 'Error: failed' });
        });

        it('does nothing when delete link is missing', async () => {
            await deleteList(dispatch, [], myListsHref);
            expect(dispatch).not.toHaveBeenCalled();
        });
    });

    describe('addItemToList', () => {
        const links = [{ rel: 'add-item', href: 'http://localhost:8081/my-lists/LIST-001' }];

        it('dispatches ADD_ITEM_TO_LIST_SUCCESS on success', async () => {
            const updatedList = { listId: 'LIST-001', name: 'My Fiction' };
            apiClient.postByHref.mockResolvedValue(updatedList);

            await addItemToList(dispatch, links, 'ITEM-001');

            expect(apiClient.postByHref).toHaveBeenCalledWith('http://localhost:8081/my-lists/LIST-001', { itemId: 'ITEM-001' });
            expect(dispatch).toHaveBeenCalledWith({ type: ADD_ITEM_TO_LIST_SUCCESS, payload: updatedList });
        });

        it('dispatches ADD_ITEM_TO_LIST_ERROR on failure', async () => {
            apiClient.postByHref.mockRejectedValue(new Error('item not in library'));

            await addItemToList(dispatch, links, 'ITEM-999');

            expect(dispatch).toHaveBeenCalledWith({ type: ADD_ITEM_TO_LIST_ERROR, payload: 'Error: item not in library' });
        });

        it('does nothing when add-item link is missing', async () => {
            await addItemToList(dispatch, [], 'ITEM-001');
            expect(dispatch).not.toHaveBeenCalled();
        });
    });
});
