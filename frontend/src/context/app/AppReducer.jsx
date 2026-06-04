import { BOOTSTRAP_SUCCESS, BOOTSTRAP_ERROR } from '../lists/ListsActions';

export const initialAppState = {
    myListsHref: null,
    createListHref: null,
    genresHref: null,
    libraryHref: null,
};

export function appReducer(state, action) {
    switch (action.type) {
        case BOOTSTRAP_SUCCESS:
            return {
                ...state,
                myListsHref: action.payload?.['collection']?.href ?? null,
                createListHref: action.payload?.['create-list']?.href ?? null,
                genresHref: action.payload?.['genres']?.href ?? null,
                libraryHref: action.payload?.['library']?.href ?? null,
                directSalesHref: action.payload?.['direct-sales']?.href ?? null,
                directSalesWithoutPriceHref: action.payload?.['direct-sales-without-price']?.href ?? null,
            };
        case BOOTSTRAP_ERROR:
            return { ...state };
        default:
            return state;
    }
}