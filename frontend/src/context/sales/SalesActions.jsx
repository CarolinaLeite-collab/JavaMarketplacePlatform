import { apiClient } from '../../services/apiClient';


export const GET_LIBRARY_ITEMS_SUCCESS = 'GET_LIBRARY_ITEMS_SUCCESS';
export const GET_LIBRARY_ITEMS_ERROR = 'GET_LIBRARY_ITEMS_ERROR';
export const CREATE_DIRECT_SALE_SUCCESS = 'CREATE_DIRECT_SALE_SUCCESS';
export const CREATE_DIRECT_SALE_ERROR = 'CREATE_DIRECT_SALE_ERROR';
export const CLEAR_SALES_MESSAGES = 'CLEAR_SALES_MESSAGES';


function extractErrorMessage(error) {
    const rawMessage = String(error?.message ?? error);


    try {
        const parsed = JSON.parse(rawMessage);
        return parsed.message || rawMessage;
    } catch {
        return rawMessage;
    }
}


export function getLibraryItemsSuccess(payload) {
    return { type: GET_LIBRARY_ITEMS_SUCCESS, payload };
}


export function getLibraryItemsError(error) {
    return { type: GET_LIBRARY_ITEMS_ERROR, payload: error };
}


export function createDirectSaleSuccess(payload) {
    return { type: CREATE_DIRECT_SALE_SUCCESS, payload };
}


export function createDirectSaleError(error) {
    return { type: CREATE_DIRECT_SALE_ERROR, payload: error };
}


export function clearSalesMessages() {
    return { type: CLEAR_SALES_MESSAGES };
}


export async function getMyLibraryItems(dispatch) {
    try {
        const result = await apiClient.getMyLibraryItemsForSale();
        dispatch(getLibraryItemsSuccess(result));
    } catch (e) {
        dispatch(getLibraryItemsError(extractErrorMessage(e)));
    }
}


export async function createDirectSale(dispatch, body) {
    try {
        const result = await apiClient.createDirectSales(body);
        dispatch(createDirectSaleSuccess(result));
        return true;
    } catch (e) {
        dispatch(createDirectSaleError(extractErrorMessage(e)));
        return false;
    }
}
