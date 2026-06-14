import {createContext, useContext} from 'react';
import {initialState} from './library/LibraryReducer';

/**
 * Application-level context containing globally discovered backend links and shared state.
 *
 * HATEOAS links exposed here are populated during application bootstrap and reused
 * by feature workflows that need backend entry points, such as lists, direct sales,
 * library access, and item registration.
 */

const AppContext = createContext({
    state: {
        app: {
            myListsHref: null,
            createListHref: null,
            genresHref: null,
            libraryHref: null,
            libraryAddHref: null,
            createPublicationHref: null,
            authorsHref: null,
            createEditionHref: null,
            createItemHref: null,
            publicationTypesHref: null,
            publishingCompaniesHref: null,
            directSalesHref: null,
            directSalesWithoutPriceHref: null,
        },
        lists: {
            lists: [],
            genres: [],
            error: null,
        },
        cart: {
            items: [],
        },
    },
    dispatch: () => {}
});

export default AppContext;

export const LibraryContext = createContext({
    state: initialState,
    dispatch: () => {}
});

export const useLibrary = () => useContext(LibraryContext);