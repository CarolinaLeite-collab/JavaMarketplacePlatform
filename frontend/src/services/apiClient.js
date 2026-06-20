export const BASE_URL = 'http://localhost:8081';
export let USER_ID = 'pedro@aeiou.com'; // temporary until having authorization

/**
 * Central API client used by the frontend to communicate with the backend.
 *
 * It exposes fixed entry-point calls used during bootstrap and generic HATEOAS
 * helpers that operate on backend-provided hrefs. HATEOAS helpers should be
 * preferred whenever a link has already been discovered from the API.
 */

export function setUserId(id) {
    console.log('switching user to:', id);
    USER_ID = id;
}

// Entry point functions (hardcoded paths - initial discovery)
async function optionsByPath(path) {
    console.log('optionsByPath - current USER_ID:', USER_ID);
    const response = await fetch(`${BASE_URL}${path}?email=${USER_ID}`, {
        method: 'OPTIONS',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    const text = await response.text();
    return JSON.parse(text);
}

async function getPublic(path) {
    const response = await fetch(`${BASE_URL}${path}`);
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function getPrivate(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function post(path, body) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }
    if (response.status === 204) return null;
    return response.json();
}

// HATEOAS functions (use full href from backend response links)
async function getByHref(href) {
    console.log('getByHref:', href, 'USER_ID:', USER_ID);
    const response = await fetch(href, {
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function postByHref(href, body) {
    const response = await fetch(href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }
    if (response.status === 204) return null;
    return response.json();
}

async function patchByHref(href, body) {
    const response = await fetch(href, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage || `${response.status}`);
    }
    if (response.status === 204) return null;
    return response.json();
}

async function patchNoBodyByHref(href) {
    const response = await fetch(href, {
        method: 'PATCH',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function deleteByHref(href) {
    const response = await fetch(href, {
        method: 'DELETE',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    return null;
}

function extractIdFromSelfLink(response) {
    const href = response._links.self.href;
    const url = new URL(href);

    return url.pathname.split('/').filter(Boolean).pop();
}

// Contract endpoints
export const apiClient = {
    // Entry points — hardcoded
    getRootOptions: () => optionsByPath('/api'),
    getListsOptions: () => optionsByPath('/my-lists'),
    getGenres: () => getPublic('/genres'),
    getMyLibraryItemsForSale: () => getPrivate('/items/my-library'),
    getLibraryOptions: () => optionsByPath('/my-library'),
    createDirectSales: (body) => post('/direct-sales', body),
    getDirectSales: () => getPublic('/direct-sales'),
    getActiveDirectSales: () => getPrivate('/direct-sales/active'),
    getAuctions: () => getPublic('/auctions'),
    getPublishingCompanies: () => getPublic('/publishingCompanies'),
    getItemById: (itemId) => getPublic(`/items/${itemId}`),
    getEditionById: (editionId) => getPublic(`/editions/${editionId}`),
    getAuctionOptions: (auctionId) => optionsByPath(`/auctions/${auctionId}`),
    getAuctionById: (auctionId) => getPublic(`/auctions/${auctionId}`),
    getPublishingCompanyById: (id) => getPublic(`/publishingCompanies/${id}`),

    /**
     * Extracts the resource identifier from a HATEOAS self link.
     *
     * This is used after creating resources whose ids are required by subsequent
     * workflow steps, such as creating an edition after creating a publication.
     */

    extractIdFromSelfLink: (response) => extractIdFromSelfLink(response),

    // HATEOAS — use full href from backend response links
    getByHref: (href) => getByHref(href),
    postByHref: (href, body) => postByHref(href, body),
    patchByHref: (href, body) => patchByHref(href, body),
    patchNoBodyByHref: (href) => patchNoBodyByHref(href),
    deleteByHref: (href) => deleteByHref(href),
};