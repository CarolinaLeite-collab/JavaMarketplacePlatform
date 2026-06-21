import { apiClient } from '../../services/apiClient';

export const ADD_TO_CART = 'ADD_TO_CART';
export const REMOVE_FROM_CART = 'REMOVE_FROM_CART';
export const CLEAR_CART = 'CLEAR_CART';

export const LOAD_CART = 'LOAD_CART';

export async function loadCart(dispatch, shoppingCartHref) {
    if (!shoppingCartHref) {
        dispatch({ type: LOAD_CART, payload: [] });
        return;
    }

    const cart = await apiClient.getByHref(shoppingCartHref);

    const rawLinks = cart?._links?.['shopping-cart-line'];

    const links = !rawLinks
        ? []
        : Array.isArray(rawLinks)
            ? rawLinks
            : [rawLinks];

    const items = await Promise.all(
        links.map(async link => {
            const cartLine = await apiClient.getByHref(link.href);

            return {
                id: cartLine.directSaleId,
                name: cartLine.directSaleId,
                price: `${cartLine.priceAtAddition} ${cartLine.currency}`,
                image: null,
                deleteHref: cartLine?._links?.self?.href,
            };
        })
    );

    dispatch({
        type: LOAD_CART,
        payload: items,
    });
}