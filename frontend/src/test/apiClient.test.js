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
                                 text = null
                             }) => ({
    ok,
    status,
    json: vi.fn().mockResolvedValue(json),
    text: vi.fn().mockResolvedValue(text)
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
});