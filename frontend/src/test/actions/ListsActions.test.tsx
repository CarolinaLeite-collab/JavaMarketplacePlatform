import {beforeEach, describe, expect, it, vi} from 'vitest';
import * as Actions from '../../context/lists/ListsActions.jsx';
import {apiClient} from '../../services/apiClient.js';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getRootOptions: vi.fn(),
        getListsOptions: vi.fn(),
        getByHref: vi.fn(),
        postByHref: vi.fn(),
        patchByHref: vi.fn(),
        patchNoBodyByHref: vi.fn(),
        deleteByHref: vi.fn(),
    }
}));

describe('ListsActions', () => {
    let dispatch: ReturnType<typeof vi.fn>;

    beforeEach(() => {
        dispatch = vi.fn();
        vi.clearAllMocks();
    });

    // ---------------------------------------------------------
    // bootstrapRoot
    // ---------------------------------------------------------
    describe('[bootstrapRoot]', () => {
        it('dispatches BOOTSTRAP_SUCCESS on success', async () => {
            apiClient.getRootOptions.mockResolvedValue({ _links: { a: 1 } });

            await Actions.bootstrapRoot(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.BOOTSTRAP_SUCCESS,
                payload: { a: 1 }
            });
        });

        it('dispatches BOOTSTRAP_ERROR on failure', async () => {
            apiClient.getRootOptions.mockRejectedValue(new Error('fail'));

            await Actions.bootstrapRoot(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.BOOTSTRAP_ERROR,
                payload: 'Error: fail'
            });
        });
    });

    // ---------------------------------------------------------
    // getListsOptions
    // ---------------------------------------------------------
    describe('[getListsOptions]', () => {
        it('dispatches GET_LIST_OPTIONS_SUCCESS on success', async () => {
            apiClient.getListsOptions.mockResolvedValue({ _links: { x: 1 } });

            await Actions.getListsOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LIST_OPTIONS_SUCCESS,
                payload: { x: 1 }
            });
        });

        it('dispatches GET_LIST_OPTIONS_ERROR on failure', async () => {
            apiClient.getListsOptions.mockRejectedValue(new Error('boom'));

            await Actions.getListsOptions(dispatch);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LIST_OPTIONS_ERROR,
                payload: 'Error: boom'
            });
        });
    });

    // ---------------------------------------------------------
    // getMyLists
    // ---------------------------------------------------------
    describe('[getMyLists]', () => {
        it('dispatches GET_LISTS_SUCCESS with result', async () => {
            apiClient.getByHref.mockResolvedValue([{ id: 1 }]);

            await Actions.getMyLists(dispatch, '/lists');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LISTS_SUCCESS,
                payload: [{ id: 1 }]
            });
        });

        it('dispatches GET_LISTS_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(new Error('nope'));

            await Actions.getMyLists(dispatch, '/lists');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LISTS_ERROR,
                payload: 'Error: nope'
            });
        });
    });

    // ---------------------------------------------------------
    // createList
    // ---------------------------------------------------------
    describe('[createList]', () => {
        it('dispatches CREATE_LIST_SUCCESS and refreshes lists', async () => {
            apiClient.postByHref.mockResolvedValue({ id: 10 });
            apiClient.getByHref.mockResolvedValue([{ id: 10 }]);

            const result = await Actions.createList(dispatch, '/create', { name: 'A' }, '/lists');

            expect(result).toBe(true);
            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.CREATE_LIST_SUCCESS,
                payload: { id: 10 }
            });
            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LISTS_SUCCESS,
                payload: [{ id: 10 }]
            });
        });

        it('dispatches CREATE_LIST_ERROR on failure', async () => {
            apiClient.postByHref.mockRejectedValue(new Error('bad'));

            const result = await Actions.createList(dispatch, '/create', {}, '/lists');

            expect(result).toBe(false);
            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.CREATE_LIST_ERROR,
                payload: 'bad'
            });
        });
    });

    // ---------------------------------------------------------
    // getGenres
    // ---------------------------------------------------------
    describe('[getGenres]', () => {
        it('dispatches GET_GENRES_SUCCESS', async () => {
            apiClient.getByHref.mockResolvedValue(['Fantasy']);

            await Actions.getGenres(dispatch, '/genres');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_GENRES_SUCCESS,
                payload: ['Fantasy']
            });
        });

        it('dispatches GET_GENRES_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(new Error('err'));

            await Actions.getGenres(dispatch, '/genres');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_GENRES_ERROR,
                payload: 'Error: err'
            });
        });
    });

    // ---------------------------------------------------------
    // makeListPublic
    // ---------------------------------------------------------
    describe('[makeListPublic]', () => {
        const links = [{ rel: 'make-public', href: '/pub' }];

        it('dispatches MAKE_LIST_PUBLIC_SUCCESS', async () => {
            apiClient.patchByHref.mockResolvedValue({ ok: true });

            await Actions.makeListPublic(dispatch, links, 5);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.MAKE_LIST_PUBLIC_SUCCESS,
                payload: { ok: true }
            });
        });

        it('dispatches MAKE_LIST_PUBLIC_ERROR on failure', async () => {
            apiClient.patchByHref.mockRejectedValue(new Error('fail'));

            await Actions.makeListPublic(dispatch, links, 5);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.MAKE_LIST_PUBLIC_ERROR,
                payload: 'Error: fail'
            });
        });
    });

    // ---------------------------------------------------------
    // makeListPrivate
    // ---------------------------------------------------------
    describe('[makeListPrivate]', () => {
        const links = [{ rel: 'make-private', href: '/priv' }];

        it('dispatches MAKE_LIST_PRIVATE_SUCCESS', async () => {
            apiClient.patchNoBodyByHref.mockResolvedValue({ ok: true });

            await Actions.makeListPrivate(dispatch, links);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.MAKE_LIST_PRIVATE_SUCCESS,
                payload: { ok: true }
            });
        });

        it('dispatches MAKE_LIST_PRIVATE_ERROR on failure', async () => {
            apiClient.patchNoBodyByHref.mockRejectedValue(new Error('x'));

            await Actions.makeListPrivate(dispatch, links);

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.MAKE_LIST_PRIVATE_ERROR,
                payload: 'Error: x'
            });
        });
    });

    // ---------------------------------------------------------
    // addItemToList
    // ---------------------------------------------------------
    describe('[addItemToList]', () => {
        const links = [{ rel: 'add-item', href: '/add' }];

        it('dispatches ADD_ITEM_TO_LIST_SUCCESS', async () => {
            apiClient.postByHref.mockResolvedValue({ added: true });

            await Actions.addItemToList(dispatch, links, '123');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.ADD_ITEM_TO_LIST_SUCCESS,
                payload: { added: true }
            });
        });

        it('dispatches ADD_ITEM_TO_LIST_ERROR on failure', async () => {
            apiClient.postByHref.mockRejectedValue(new Error('no'));

            await Actions.addItemToList(dispatch, links, '123');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.ADD_ITEM_TO_LIST_ERROR,
                payload: 'Error: no'
            });
        });
    });

    // ---------------------------------------------------------
    // deleteList
    // ---------------------------------------------------------
    describe('[deleteList]', () => {
        const links = [{ rel: 'delete', href: '/del' }];

        it('dispatches DELETE_LIST_SUCCESS and refreshes lists', async () => {
            apiClient.deleteByHref.mockResolvedValue();
            apiClient.getByHref.mockResolvedValue([{ id: 1 }]);

            await Actions.deleteList(dispatch, links, '/lists');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.DELETE_LIST_SUCCESS,
                payload: '/del'
            });

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_LISTS_SUCCESS,
                payload: [{ id: 1 }]
            });
        });

        it('dispatches DELETE_LIST_ERROR on failure', async () => {
            apiClient.deleteByHref.mockRejectedValue(new Error('boom'));

            await Actions.deleteList(dispatch, links, '/lists');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.DELETE_LIST_ERROR,
                payload: 'Error: boom'
            });
        });
    });

    // ---------------------------------------------------------
    // removeItemFromList
    // ---------------------------------------------------------
    describe('[removeItemFromList]', () => {
        const links = [{ rel: 'remove-item', href: '/remove' }];

        it('dispatches REMOVE_ITEM_FROM_LIST_SUCCESS', async () => {
            apiClient.patchByHref.mockResolvedValue({ removed: true });

            await Actions.removeItemFromList(dispatch, links, '123');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.REMOVE_ITEM_FROM_LIST_SUCCESS,
                payload: { removed: true }
            });
        });

        it('dispatches REMOVE_ITEM_FROM_LIST_ERROR on failure', async () => {
            apiClient.patchByHref.mockRejectedValue(new Error('err'));

            await Actions.removeItemFromList(dispatch, links, '123');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.REMOVE_ITEM_FROM_LIST_ERROR,
                payload: 'Error: err'
            });
        });
    });

    // ---------------------------------------------------------
    // getPublicLists
    // ---------------------------------------------------------
    describe('[getPublicLists]', () => {
        it('dispatches GET_PUBLIC_LISTS_SUCCESS', async () => {
            apiClient.getByHref.mockResolvedValue([{ id: 1 }]);

            await Actions.getPublicLists(dispatch, '/public');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_PUBLIC_LISTS_SUCCESS,
                payload: [{ id: 1 }]
            });
        });

        it('dispatches GET_PUBLIC_LISTS_ERROR on failure', async () => {
            apiClient.getByHref.mockRejectedValue(new Error('fail'));

            await Actions.getPublicLists(dispatch, '/public');

            expect(dispatch).toHaveBeenCalledWith({
                type: Actions.GET_PUBLIC_LISTS_ERROR,
                payload: 'Error: fail'
            });
        });
    });
});
