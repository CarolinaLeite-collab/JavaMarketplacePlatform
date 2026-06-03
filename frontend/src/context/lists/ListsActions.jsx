import { apiClient } from '../../services/apiClient';

export const GET_LIST_OPTIONS_SUCCESS = 'GET_LIST_OPTIONS_SUCCESS';
export const GET_LIST_OPTIONS_ERROR = 'GET_LIST_OPTIONS_ERROR';
export const CREATE_LIST_SUCCESS = 'CREATE_LIST_SUCCESS';
export const CREATE_LIST_ERROR = 'CREATE_LIST_ERROR';
export const GET_LISTS_SUCCESS = 'GET_LISTS_SUCCESS';
export const GET_LISTS_ERROR = 'GET_LISTS_ERROR';
export const GET_GENRES_SUCCESS = 'GET_GENRES_SUCCESS';
export const GET_GENRES_ERROR = 'GET_GENRES_ERROR';
export const MAKE_LIST_PUBLIC_SUCCESS = 'MAKE_LIST_PUBLIC_SUCCESS';
export const MAKE_LIST_PUBLIC_ERROR = 'MAKE_LIST_PUBLIC_ERROR';
export const MAKE_LIST_PRIVATE_SUCCESS = 'MAKE_LIST_PRIVATE_SUCCESS';
export const MAKE_LIST_PRIVATE_ERROR = 'MAKE_LIST_PRIVATE_ERROR';
export const DELETE_LIST_SUCCESS = 'DELETE_LIST_SUCCESS';
export const DELETE_LIST_ERROR = 'DELETE_LIST_ERROR';


export function getListsSuccess(lists) {
    return { type: GET_LISTS_SUCCESS, payload: lists };
}

export function getListsError(error) {
    return { type: GET_LISTS_ERROR, payload: error };
}

export function createListSuccess(list) {
    return { type: CREATE_LIST_SUCCESS, payload: list };
}

export function createListError(error) {
    return { type: CREATE_LIST_ERROR, payload: error };
}

export function getGenresSuccess(genres) {
    return { type: GET_GENRES_SUCCESS, payload: genres };
}

export function getGenresError(error) {
    return { type: GET_GENRES_ERROR, payload: error };
}

export async function getListsOptions(dispatch) {
    try {
        const result = await apiClient.getListsOptions();
        dispatch({ type: GET_LIST_OPTIONS_SUCCESS, payload: result._links });
    } catch (e) {

        dispatch({ type: GET_LIST_OPTIONS_ERROR, payload: String(e) });
    }
}


export async function createList(dispatch, href, body, myListsHref) {
    try {
        const result = await apiClient.postByHref(href, body);
        dispatch(createListSuccess(result));
        await getMyLists(dispatch, myListsHref);
        return true;
    } catch (e) {
        dispatch(createListError(e.message));
        return false;
    }
}

export async function getMyLists(dispatch, href) {
    console.log('getMyLists called with href:', href);
    try {
        const result = await apiClient.getByHref(href);
        console.log('getMyLists result:', result);
        dispatch(getListsSuccess(result ?? []));
    } catch (e) {
        console.log('getMyLists error:', e);
        dispatch(getListsError(String(e)));
    }
}

export async function getGenres(dispatch) {
    try {
        const result = await apiClient.getGenres();
        dispatch(getGenresSuccess(result));
    } catch (e) {
        dispatch(getGenresError(String(e)));
    }
}

export async function makeListPublic(dispatch, links, days) {
    const href = links?.find(l => l.rel === 'make-public')?.href;
    if (!href) return;
    try {
        const result = await apiClient.patchByHref(href, { sharedUntil: days });
        dispatch({ type: MAKE_LIST_PUBLIC_SUCCESS, payload: result });
    } catch (e) {
        dispatch({ type: MAKE_LIST_PUBLIC_ERROR, payload: String(e) });
    }
}

export async function makeListPrivate(dispatch, links) {
    const href = links?.find(l => l.rel === 'make-private')?.href;
    if (!href) return;
    try {
        const result = await apiClient.patchNoBodyByHref(href);
        dispatch({ type: MAKE_LIST_PRIVATE_SUCCESS, payload: result });
    } catch (e) {
        dispatch({ type: MAKE_LIST_PRIVATE_ERROR, payload: String(e) });
    }
}

export async function deleteList(dispatch, links, myListsHref) {
    const href = links?.find(l => l.rel === 'delete')?.href;
    if (!href) return;
    try {
        await apiClient.deleteByHref(href);
        dispatch({ type: DELETE_LIST_SUCCESS, payload: href });
        await getMyLists(dispatch, myListsHref);
    } catch (e) {
        dispatch({ type: DELETE_LIST_ERROR, payload: String(e) });
    }
}