import {BOOTSTRAP_ERROR, BOOTSTRAP_SUCCESS, GET_LIST_OPTIONS_SUCCESS} from '../lists/ListsActions';

export const initialAppState = {
    myListsHref: null,
    publicListsHref: null,
    createListHref: null,
    genresHref: null,
    libraryHref: null,
    directSalesHref: null,
    directSalesWithoutPriceHref: null,
    createEditionHref: null,
    createItemHref: null,
    publicationTypesHref: null,
    publishingCompaniesHref: null,
    createPublicationHref: null,
    authorsHref: null,
    libraryAddHref: null,
    shoppingCartHref: null,
};

/**
 * Reducer responsible for storing application-wide HATEOAS links discovered at bootstrap.
 *
 * The reducer maps backend root OPTIONS links into stable frontend state fields so
 * components can execute workflows without hardcoding endpoint paths.
 */

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
                createPublicationHref: action.payload?.['createPublication']?.href ?? null,
                authorsHref: action.payload?.['authors']?.href ?? null,
                libraryAddHref: action.payload?.['library-add']?.href ?? null,
                createEditionHref: action.payload?.['edition-create']?.href ?? null,
                createItemHref: action.payload?.['createItem']?.href ?? null,
                publicationTypesHref: action.payload?.['publication-types']?.href ?? null,
                publishingCompaniesHref:
                    action.payload?.['publishingCompanies']?.href ??
                    'http://localhost:8081/publishingCompanies',
                shoppingCartHref:
                    action.payload?.['shopping-cart']?.href ?? null,
            };

        case GET_LIST_OPTIONS_SUCCESS:
            return {
                ...state,
                myListsHref:     action.payload?.['collection']?.href    ?? state.myListsHref,
                createListHref:  action.payload?.['create-list']?.href   ?? state.createListHref,
                publicListsHref: action.payload?.['public-lists']?.href  ?? state.publicListsHref,
            };
        case BOOTSTRAP_ERROR:
            return { ...state };
        default:
            return state;
    }
}