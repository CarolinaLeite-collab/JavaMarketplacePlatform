import {
    CLEAR_SALES_MESSAGES,
    CREATE_AUCTION_ERROR,
    CREATE_AUCTION_SUCCESS,
    CREATE_DIRECT_SALE_ERROR,
    CREATE_DIRECT_SALE_SUCCESS,
    GET_LIBRARY_ITEMS_ERROR,
    GET_LIBRARY_ITEMS_SUCCESS
} from './SalesActions.jsx';


export const initialSalesState = {
    libraryItems: [],
    error: null,
    successMessage: null,
};

function getLink(links, rel) {
    return links?.find((link) => link.rel === rel)?.href ?? null;
}

function mapLibraryItem(item) {
    const links = item.links ?? [];

    return {
        value: item.itemId,
        label: item.title,
        picture: item.picture,
        saleStatus: item.saleStatus,
        selfHref: getLink(links, 'self'),
        createDirectSaleHref: getLink(links, 'create-direct-sale'),
        createAuctionHref: getLink(links, 'create-auction'),
        links,
    };
}

export function salesReducer(state, action) {
    switch (action.type) {
        case GET_LIBRARY_ITEMS_SUCCESS: {
            const items = (action.payload ?? []).map(mapLibraryItem);

            return {
                ...state,
                error: null,
                libraryItems: items,
            };
        }


        case GET_LIBRARY_ITEMS_ERROR:
            return { ...state, error: action.payload };


        case CREATE_DIRECT_SALE_SUCCESS:
            return { ...state, error: null, successMessage: 'The item was successfully put on direct sale.', };


        case CREATE_DIRECT_SALE_ERROR:
            return { ...state, error: action.payload };

        case CREATE_AUCTION_SUCCESS:
            return { ...state, error: null, successMessage: 'The item was successfully put on auction.' };

        case CREATE_AUCTION_ERROR:
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
