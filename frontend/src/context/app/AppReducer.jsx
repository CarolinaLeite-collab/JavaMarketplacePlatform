import {BOOTSTRAP_ERROR, BOOTSTRAP_SUCCESS} from '../lists/ListsActions';

export const initialAppState = {
    myListsHref: null,
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
                createPublicationHref: action.payload?.['createPublication']?.href ?? null,
                authorsHref: action.payload?.['authors']?.href ?? null,
                libraryAddHref: action.payload?.['library-add']?.href ?? null,
                createEditionHref: action.payload?.['edition-create']?.href ?? null,
                createItemHref: action.payload?.['createItem']?.href ?? null,
                publicationTypesHref: action.payload?.['publication-types']?.href ?? null,
                publishingCompaniesHref:
                    action.payload?.['publishingCompanies']?.href ??
                    'http://localhost:8081/publishingCompanies',            };
        case BOOTSTRAP_ERROR:
            return { ...state };
        default:
            return state;
    }
}