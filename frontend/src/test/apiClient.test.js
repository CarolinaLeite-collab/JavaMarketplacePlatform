import { describe, it, expect, vi, beforeEach } from 'vitest';
import { apiClient, BASE_URL, setUserId } from '../services/apiClient';

vi.stubGlobal('fetch', vi.fn());
const mockFetch = global.fetch;

const USER_ID = 'pedro@aeiou.com';

/**
 * Unified fetch mock
 */
const createFetchResponse = ({
                                 ok = true,
                                 status = 200,
                                 json = null,
                                 text = null,
                                 headers = {
                                     get: vi.fn().mockReturnValue(null)
                                 }
                             }) => ({
    ok,
    status,
    json: vi.fn().mockResolvedValue(json),
    text: vi.fn().mockResolvedValue(text),
    headers
});

beforeEach(() => {
    mockFetch.mockClear();
    setUserId(USER_ID);
});

describe('apiClient', () => {

    describe('getListsOptions', () => {

        it('calls OPTIONS on my-lists endpoint', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    text: JSON.stringify({ links: [] })
                })
            );

            await apiClient.getListsOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-lists?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );
        });

        it('throws error when getListsOptions fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 500 })
            );

            await expect(apiClient.getListsOptions())
                .rejects.toThrow('500');
        });

    });

    describe('getLibraryOptions', () => {

        it('calls OPTIONS on /my-library', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    text: JSON.stringify({ _links: {} })
                })
            );

            await apiClient.getLibraryOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-library?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );
        });

    });

    describe('getGenres', () => {

        it('calls correct URL without auth header', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: { genres: [] }
                })
            );

            await apiClient.getGenres();

            expect(mockFetch).toHaveBeenCalledWith(`${BASE_URL}/genres`);
        });

    });

    describe('getPublishingCompanies', () => {

        it('calls publishing companies endpoint without auth header', async () => {
            const response = [{ publishingCompanyId: 'PUB-001' }];

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: response
                })
            );

            const result = await apiClient.getPublishingCompanies();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/publishingCompanies`
            );
            expect(result).toEqual(response);
        });

        it('throws when getPublishingCompanies fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 404 })
            );

            await expect(apiClient.getPublishingCompanies())
                .rejects.toThrow('404');
        });

        it('returns null when getPublishingCompanies receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getPublishingCompanies();

            expect(result).toBeNull();
        });

    });

    describe('getByHref', () => {

        it('calls href with X-User-Id', async () => {
            const href = `${BASE_URL}/my-library/ITEM-001`;

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: { itemId: 'ITEM-001' }
                })
            );

            await apiClient.getByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    headers: {
                        'X-User-Id': USER_ID
                    }
                })
            );
        });

        it('throws when getByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 404 })
            );

            await expect(apiClient.getByHref(`${BASE_URL}/items/ITEM-001`))
                .rejects.toThrow('404');
        });

        it('returns null when getByHref receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getByHref(`${BASE_URL}/items/ITEM-001`);

            expect(result).toBeNull();
        });

    });

    describe('getRootOptions', () => {

        it('fetches root options successfully', async () => {
            const response = {
                _links: {
                    genres: {
                        href: `${BASE_URL}/genres`
                    }
                }
            };

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    text: JSON.stringify(response)
                })
            );

            const result = await apiClient.getRootOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/api?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );

            expect(result).toEqual(response);
        });

        it('throws when root options fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 500 })
            );

            await expect(apiClient.getRootOptions())
                .rejects.toThrow('500');
        });

    });

    describe('postByHref', () => {
        it('posts body to href with auth and content-type headers', async () => {
            const href = `${BASE_URL}/publications`;
            const body = { title: 'Dune' };
            const response = { publicationId: 'PUB-001' };

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 201,
                    json: response
                })
            );

            const result = await apiClient.postByHref(href, body);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-User-Id': USER_ID
                    },
                    body: JSON.stringify(body)
                }
            );

            expect(result).toEqual(response);
        });

        it('throws response text when postByHref fails', async () => {
            const href = `${BASE_URL}/publications`;

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: false,
                    status: 500,
                    text: 'backend error'
                })
            );

            await expect(apiClient.postByHref(href, {}))
                .rejects.toThrow('backend error');
        });

        it('returns null when postByHref receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.postByHref(`${BASE_URL}/my-library`, {});

            expect(result).toBeNull();
        });
    });

    describe('extractIdFromSelfLink', () => {
        it('extracts id from self link', () => {
            const response = {
                _links: {
                    self: {
                        href: `${BASE_URL}/publications/PUB-001`
                    }
                }
            };

            const result = apiClient.extractIdFromSelfLink(response);

            expect(result).toBe('PUB-001');
        });
    });

    describe('patchByHref', () => {
        it('patches body to href with auth and content-type headers', async () => {
            const href = `${BASE_URL}/my-lists/LIST-001`;
            const body = { name: 'Updated list' };
            const response = { listId: 'LIST-001', name: 'Updated list' };

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: response
                })
            );

            const result = await apiClient.patchByHref(href, body);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-User-Id': USER_ID
                    },
                    body: JSON.stringify(body)
                }
            );

            expect(result).toEqual(response);
        });

        it('throws when patchByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 500 })
            );

            await expect(apiClient.patchByHref(`${BASE_URL}/my-lists/LIST-001`, {}))
                .rejects.toThrow('500');
        });

        it('returns null when patchByHref receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.patchByHref(`${BASE_URL}/my-lists/LIST-001`, {});

            expect(result).toBeNull();
        });
    });

    describe('patchNoBodyByHref', () => {
        it('patches href without body', async () => {
            const href = `${BASE_URL}/my-lists/LIST-001/items/ITEM-001`;
            const response = { success: true };

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: response
                })
            );

            const result = await apiClient.patchNoBodyByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                {
                    method: 'PATCH',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );

            expect(result).toEqual(response);
        });

        it('throws when patchNoBodyByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 404 })
            );

            await expect(apiClient.patchNoBodyByHref(`${BASE_URL}/my-lists/LIST-001`))
                .rejects.toThrow('404');
        });

        it('returns null when patchNoBodyByHref receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.patchNoBodyByHref(`${BASE_URL}/my-lists/LIST-001`);

            expect(result).toBeNull();
        });
    });

    describe('deleteByHref', () => {
        it('deletes href with auth header', async () => {
            const href = `${BASE_URL}/my-lists/LIST-001`;

            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.deleteByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                {
                    method: 'DELETE',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );

            expect(result).toBeNull();
        });

        it('throws when deleteByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 404 })
            );

            await expect(apiClient.deleteByHref(`${BASE_URL}/my-lists/LIST-001`))
                .rejects.toThrow('404');
        });
    });

    describe('getAuctionById', () => {

        it('calls correct URL without auth header', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: { auctionId: 'AUC-001' }
                })
            );

            await apiClient.getAuctionById('AUC-001');

            expect(mockFetch).toHaveBeenCalledWith(`${BASE_URL}/auctions/AUC-001`);
        });

        it('throws when getAuctionById fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({ ok: false, status: 404 })
            );

            await expect(apiClient.getAuctionById('AUC-001'))
                .rejects.toThrow('404');
        });

        it('returns null when getAuctionById receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getAuctionById('AUC-001');

            expect(result).toBeNull();
        });

    });

    describe('getEditionById', () => {

        it('calls correct URL without auth header', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: { editionId: 'ED-001' }
                })
            );

            await apiClient.getEditionById('ED-001');

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/editions/ED-001`
            );
        });

        it('throws when getEditionById fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: false,
                    status: 404
                })
            );

            await expect(
                apiClient.getEditionById('ED-001')
            ).rejects.toThrow('404');
        });

        it('returns null when getEditionById receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getEditionById('ED-001');

            expect(result).toBeNull();
        });

    });

    describe('getPublishingCompanyById', () => {

        it('calls correct URL without auth header', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    json: { publishingCompanyId: 'PUB-001' }
                })
            );

            await apiClient.getPublishingCompanyById('PUB-001');

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/publishingCompanies/PUB-001`
            );
        });

        it('throws when getPublishingCompanyById fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: false,
                    status: 404
                })
            );

            await expect(
                apiClient.getPublishingCompanyById('PUB-001')
            ).rejects.toThrow('404');
        });

        it('returns null when getPublishingCompanyById receives 204', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getPublishingCompanyById('PUB-001');

            expect(result).toBeNull();
        });

    });

    describe('getAuctionOptions', () => {

        it('calls OPTIONS on auction endpoint', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: true,
                    status: 200,
                    text: JSON.stringify({
                        _links: {}
                    })
                })
            );

            await apiClient.getAuctionOptions('AUC-001');

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/auctions/AUC-001?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );
        });

        it('throws when getAuctionOptions fails', async () => {
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    ok: false,
                    status: 500
                })
            );

            await expect(
                apiClient.getAuctionOptions('AUC-001')
            ).rejects.toThrow('500');
        });

    });

    describe('allowed methods', () => {
        it('gets allowed methods for Sales', async () => {
            const getHeader = vi.fn().mockReturnValue('GET, POST, OPTIONS');
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    headers: { get: getHeader }
                })
            );

            const result = await apiClient.getSalesAllowedMethods();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/sales?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: { 'X-User-Id': USER_ID }
                }
            );
            expect(getHeader).toHaveBeenCalledWith('Allow');
            expect(result).toEqual(['GET', 'POST', 'OPTIONS']);
        });

        it('gets allowed methods for Shopping Cart', async () => {
            const getHeader = vi.fn().mockReturnValue('GET, OPTIONS');
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    headers: { get: getHeader }
                })
            );

            const result = await apiClient.getShoppingCartAllowedMethods();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/shopping-carts?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: { 'X-User-Id': USER_ID }
                }
            );
            expect(result).toEqual(['GET', 'OPTIONS']);
        });

        it('gets allowed methods from a HATEOAS href', async () => {
            const href = `${BASE_URL}/shopping-carts/CART-1`;
            const getHeader = vi.fn().mockReturnValue('GET, PATCH, OPTIONS');
            mockFetch.mockReturnValueOnce(
                createFetchResponse({
                    headers: { get: getHeader }
                })
            );

            const result = await apiClient.getAllowedMethodsByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(href, {
                method: 'OPTIONS',
                headers: { 'X-User-Id': USER_ID }
            });
            expect(result).toEqual(['GET', 'PATCH', 'OPTIONS']);
        });

        it('returns an empty array when the Allow header is unavailable', async () => {
            mockFetch.mockReturnValueOnce(createFetchResponse({}));

            const result = await apiClient.getSalesAllowedMethods();

            expect(result).toEqual([]);
        });
    });
});