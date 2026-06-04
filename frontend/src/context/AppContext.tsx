import {createContext, useContext} from 'react';
import {initialState} from './library/LibraryReducer';

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
    },
    dispatch: () => {}
});

export default AppContext;

export const LibraryContext = createContext({
    state: initialState,
    dispatch: () => {}
});

export const useLibrary = () => useContext(LibraryContext);