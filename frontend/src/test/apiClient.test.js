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

});