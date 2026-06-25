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

    const allowedMethods = await apiClient.getShoppingCartAllowedMethods();

    //Options - confirms if user can do the GET
    if (!allowedMethods.includes('GET')) {
        dispatch({
            type: LOAD_CART,
            payload: [],
        });
        return;
    }
    //

    const cartDiscovery = await apiClient.getByHref(shoppingCartHref);

    const cartHref = cartDiscovery?._links?.self?.href;

    if (!cartHref) {
        dispatch({
            type: LOAD_CART,
            payload: [],
        });
        return;
    }

    const cart = await apiClient.getByHref(cartHref);

    const rawLinks = cart?._links?.['shopping-cart-line'];

    const links = !rawLinks
        ? []
        : Array.isArray(rawLinks)
            ? rawLinks
            : [rawLinks];

    const items = await Promise.all(
        links.map(async link => {
            const cartLine = await apiClient.getByHref(link.href);

            const directSaleHref =
                cartLine?._links?.['direct-sale']?.href;

            const directSale = directSaleHref
                ? await apiClient.getByHref(directSaleHref)
                : null;

            const rawItemLinks =
                directSale?._links?.item;

            const itemLinks = !rawItemLinks
                ? []
                : Array.isArray(rawItemLinks)
                    ? rawItemLinks
                    : [rawItemLinks];

            const item = itemLinks[0]?.href
                ? await apiClient.getByHref(itemLinks[0].href)
                : null;

            return {
                id: cartLine.directSaleId,
                name: item?.title ?? cartLine.directSaleId,
                price: `${cartLine.priceAtAddition} ${cartLine.currency}`,
                priceValue: cartLine.priceAtAddition,
                currency: cartLine.currency,
                image: item?.picture ?? null,
                deleteHref: cartLine?._links?.self?.href,
            };
        })
    );

    dispatch({
        type: LOAD_CART,
        payload: items,
    });
}