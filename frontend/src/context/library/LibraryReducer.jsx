export const initialState = {
    items: [],
    details: {},
    loading: false,
    error: null
};

export function libraryReducer(state, action) {
    switch (action.type) {

        case 'LOADING':
            return { ...state, loading: true, error: null };

        case 'FETCH_LIBRARY_SUCCESS':
            return { ...state, loading: false, items: action.payload };

        case 'FETCH_LIBRARY_ERROR':
            return { ...state, loading: false, error: action.payload };

        case 'FETCH_DETAIL_SUCCESS':
            return {
                ...state,
                details: {
                    ...state.details,
                    [action.payload.itemId]: action.payload.detail
                }
            };

        case 'FETCH_DETAIL_ERROR':
            return { ...state, error: action.payload };

        default:
            return state;
    }
}