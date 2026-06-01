import { createContext } from 'react';

const AppContext = createContext({
    state: {
        lists: {
            lists: [],
            genres: [],
            error: null,
        }
    },
    dispatch: () => {}
});

export default AppContext;