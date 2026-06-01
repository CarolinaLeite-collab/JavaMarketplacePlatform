import { useReducer } from 'react';
import AppContext from './AppContext';
import { listsReducer, initialListsState } from './lists/ListsReducer';

const initialState = {
    lists: initialListsState,
};

function rootReducer(state, action) {
    return {
        lists: listsReducer(state.lists, action),
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