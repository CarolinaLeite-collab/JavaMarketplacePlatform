import {createContext, useContext} from 'react';
import {initialState} from './library/LibraryReducer';
import { RowData } from '../components/lists/tablelist/TableList.tsx';

interface AppState {
    app: {
        myListsHref: string | null;
        publicListsHref: string | null;
        createListHref: string | null;
        genresHref: string | null;
        libraryHref: string | null;
        libraryAddHref: string | null;
        createPublicationHref: string | null;
        authorsHref: string | null;
        createEditionHref: string | null;
        createItemHref: string | null;
        publicationTypesHref: string | null;
        publishingCompaniesHref: string | null;
        directSalesHref: string | null;
        directSalesWithoutPriceHref: string | null;
    };
    lists: {
        lists: RowData[];
        publicLists: RowData[];
        genres: { value: string; label: string }[];
        error: string | null;
    };
}

interface AppContextType {
    state: AppState;
    dispatch: React.Dispatch<any>;
}

const defaultState: AppState = {
    app: {
        myListsHref: null,
        publicListsHref: null,
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
        publicLists: [],
        genres: [],
        error: null,
    },

};

const AppContext = createContext<AppContextType>({
    state: defaultState,
    dispatch: () => {}
});

export default AppContext;
export type { AppState };

export const LibraryContext = createContext({
    state: initialState,
    dispatch: () => {}
});

export const useLibrary = () => useContext(LibraryContext);