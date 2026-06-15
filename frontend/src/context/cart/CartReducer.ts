import { ADD_TO_CART, REMOVE_FROM_CART, CLEAR_CART } from './CartActions';

export const initialCartState = {
    items: [],
};

export function cartReducer(state, action) {
    switch (action.type) {
        case ADD_TO_CART:
            const exists = state.items.find(i => i.id === action.payload.id);
            if (exists) return state; // already in cart
            return { ...state, items: [...state.items, action.payload] };

        case REMOVE_FROM_CART:
            return { ...state, items: state.items.filter(i => i.id !== action.payload.id) };

        case CLEAR_CART:
            return { ...state, items: [] };

        default:
            return state;
    }
}