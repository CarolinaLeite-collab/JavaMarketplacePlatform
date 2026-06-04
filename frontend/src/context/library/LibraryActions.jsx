import {apiClient} from '../../services/apiClient';

// Actions types
export const FETCH_LIBRARY_SUCCESS  = 'FETCH_LIBRARY_SUCCESS';
export const FETCH_LIBRARY_ERROR    = 'FETCH_LIBRARY_ERROR';
export const FETCH_DETAIL_SUCCESS   = 'FETCH_DETAIL_SUCCESS';
export const FETCH_DETAIL_ERROR     = 'FETCH_DETAIL_ERROR';
export const GET_LIBRARY_OPTIONS_SUCCESS = 'GET_LIBRARY_OPTIONS_SUCCESS';
export const GET_LIBRARY_OPTIONS_ERROR   = 'GET_LIBRARY_OPTIONS_ERROR';

export const ADD_ITEM_SUCCESS = 'ADD_ITEM_SUCCESS';
export const ADD_ITEM_ERROR = 'ADD_ITEM_ERROR';



export function fetchLibrarySuccess(items) {
    return { type: FETCH_LIBRARY_SUCCESS, payload: items };
}

export function fetchLibraryError(error) {
    return { type: FETCH_LIBRARY_ERROR, payload: error };
}

export function fetchDetailSuccess(itemId, detail) {
    return { type: FETCH_DETAIL_SUCCESS, payload: { itemId, detail } };
}

export function fetchDetailError(error) {
    return { type: FETCH_DETAIL_ERROR, payload: error };
}

export function addItemSuccess(item) {
    return { type: ADD_ITEM_SUCCESS, payload: item };
}

export function addItemError(error) {
    return { type: ADD_ITEM_ERROR, payload: error };
}

export async function getLibrary(dispatch, href) {

    if (!href) {
        dispatch(fetchLibraryError('Missing library href'));
        return;
    }

    try {
        const data = await apiClient.getByHref(href);

        const embedded = data?._embedded;
        const key = embedded ? Object.keys(embedded)[0] : null;
        const items = key ? embedded[key] : [];

        dispatch(fetchLibrarySuccess(items));
    } catch (e) {
        dispatch(fetchLibraryError(String(e)));
    }
}

export async function getLibraryDetail(dispatch, href, itemId) {
    try {
        const data = await apiClient.getByHref(href);
        dispatch(fetchDetailSuccess(itemId, data));
    } catch (e) {
        dispatch(fetchDetailError(String(e)));
    }
}

export async function getLibraryOptions(dispatch) {
    try {
        const result = await apiClient.getLibraryOptions();
        dispatch({ type: GET_LIBRARY_OPTIONS_SUCCESS, payload: result._links });
    } catch (e) {
        dispatch({ type: GET_LIBRARY_OPTIONS_ERROR, payload: String(e) });
    }
}