import { apiClient } from '../../services/apiClient';

// Actions types
export const LOADING                = 'LOADING';
export const FETCH_LIBRARY_SUCCESS  = 'FETCH_LIBRARY_SUCCESS';
export const FETCH_LIBRARY_ERROR    = 'FETCH_LIBRARY_ERROR';
export const FETCH_DETAIL_SUCCESS   = 'FETCH_DETAIL_SUCCESS';
export const FETCH_DETAIL_ERROR     = 'FETCH_DETAIL_ERROR';



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


export async function getLibrary(dispatch) {
    dispatch({ type: LOADING });
    try {
        const data = await apiClient.getLibrary();
        const embedded = data._embedded;
        const key = Object.keys(embedded)[0];
        dispatch(fetchLibrarySuccess(embedded[key]));
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