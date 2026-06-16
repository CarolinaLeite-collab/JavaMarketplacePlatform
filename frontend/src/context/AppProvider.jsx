import {useEffect, useReducer} from 'react';
import AppContext, {LibraryContext} from './AppContext';
import {appReducer, initialAppState} from './app/AppReducer';
import {initialListsState, listsReducer} from './lists/ListsReducer';
import {initialSalesState, salesReducer} from './sales/SalesReducer.jsx';
import {initialState as libraryInitialState, libraryReducer} from './library/LibraryReducer';
import {bootstrapRoot} from './lists/ListsActions';
import {useUser} from './UserContext';
import {cartReducer, initialCartState} from './cart/CartReducer';

const initialState = {
    app: initialAppState,
    lists: initialListsState,
    sales: initialSalesState,
    cart: initialCartState,
};

function rootReducer(state, action) {
    return {
        app: appReducer(state.app, action),
        lists: listsReducer(state.lists, action),
        sales: salesReducer(state.sales, action),
        cart: cartReducer(state.cart, action),
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