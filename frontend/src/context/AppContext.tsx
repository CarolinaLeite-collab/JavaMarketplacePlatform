import { createContext, useContext } from 'react';
import { initialState } from './library/LibraryReducer';
import { initialSalesState } from './sales/SalesReducer';

const AppContext = createContext({
    state: {
        lists: {
            lists: [],
            genres: [],
            createListHref: null,
            myListsHref: null,
            error: null,
            loading: false,
        },
        sales: initialSalesState
    },
    dispatch: () => {}
});
export default AppContext;

export const LibraryContext = createContext({
    state: initialState,
    dispatch: () => {}
});

export const useLibrary = () => useContext(LibraryContext);