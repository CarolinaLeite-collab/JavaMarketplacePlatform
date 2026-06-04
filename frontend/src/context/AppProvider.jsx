import { useReducer, useEffect } from 'react';
import AppContext, { LibraryContext } from './AppContext';
import { appReducer, initialAppState } from './app/AppReducer';
import { listsReducer, initialListsState } from './lists/ListsReducer';
import { salesReducer, initialSalesState } from './sales/SalesReducer.jsx';
import { libraryReducer, initialState as libraryInitialState } from './library/LibraryReducer';
import { bootstrapRoot } from './lists/ListsActions';
import { useUser } from './UserContext';

const initialState = {
    app: initialAppState,
    lists: initialListsState,
    sales: initialSalesState
};

function rootReducer(state, action) {
    return {
        app: appReducer(state.app, action),
        lists: listsReducer(state.lists, action),
        sales: salesReducer(state.sales, action),
    };
}

export function AppProvider({ children }) {
    const [state, dispatch] = useReducer(rootReducer, initialState);
    const { currentUser } = useUser();

    useEffect(() => {
        bootstrapRoot(dispatch);
    }, [currentUser]);

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