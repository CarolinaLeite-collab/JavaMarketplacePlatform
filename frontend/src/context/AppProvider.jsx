import { useReducer } from 'react';
import AppContext from './AppContext';
import { listsReducer, initialListsState } from './lists/ListsReducer';
import { salesReducer, initialSalesState } from './sales/SalesReducer.jsx';
import { LibraryContext } from './AppContext';
import { libraryReducer, initialState as libraryInitialState } from './library/LibraryReducer';

const initialState = {
    lists: initialListsState,
    sales: initialSalesState
};

function rootReducer(state, action) {
    return {
        lists: listsReducer(state.lists, action),
        sales: salesReducer(state.sales, action),
    };
}

export function AppProvider({ children }) {
    const [state, dispatch] = useReducer(rootReducer, initialState);

    return (
        <AppContext.Provider value={{ state, dispatch }}>
            {children}
        </AppContext.Provider>
    );
}

export function LibraryProvider({ children }) {
    const [state, dispatch] = useReducer(libraryReducer, libraryInitialState);

    return (
        <LibraryContext.Provider value={{ state, dispatch }}>
            {children}
        </LibraryContext.Provider>
    );
}