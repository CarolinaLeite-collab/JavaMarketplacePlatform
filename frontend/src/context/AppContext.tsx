import { createContext } from 'react';

const AppContext = createContext({
    state: {
        lists: {
            lists: [],
            genres: [],
            createListHref: null,
            error: null,
            loading: false,
        }
    },
    dispatch: () => {}
});
export default AppContext;