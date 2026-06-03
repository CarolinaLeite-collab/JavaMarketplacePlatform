import {
    GET_LIBRARY_ITEMS_SUCCESS,
    GET_LIBRARY_ITEMS_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    CREATE_DIRECT_SALE_ERROR,
    CLEAR_SALES_MESSAGES
} from './SalesActions.jsx';


export const initialSalesState = {
    libraryItems: [],
    error: null,
    successMessage: null,
};


function mapLibraryItem(item) {
    return {
        value: item.itemId,
        label: item.title,
        picture: item.picture,
        href: item._links?.self?.href ?? null,
    };
}


export function salesReducer(state, action) {
    switch (action.type) {
        case GET_LIBRARY_ITEMS_SUCCESS: {
            const items = action.payload?._embedded?.libraryItemSummaryDTOList ?? [];
            return {
                ...state,
                error: null,
                libraryItems: items.map(mapLibraryItem),
            };
        }


        case GET_LIBRARY_ITEMS_ERROR:
            return { ...state, error: action.payload };


        case CREATE_DIRECT_SALE_SUCCESS:
            return { ...state, error: null };


        case CREATE_DIRECT_SALE_ERROR:
            return { ...state, error: action.payload };


        case CLEAR_SALES_MESSAGES:
            return {
                ...state,
                error: null,
                successMessage: null,
            };


        default:
            return state;
    }
}
