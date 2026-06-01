import { describe, it, expect, vi, beforeEach } from 'vitest';
import { apiClient, BASE_URL, USER_ID } from '../services/apiClient';


// simulate a Vitest function
const mockFetch = vi.fn();
vi.stubGlobal('fetch', mockFetch);

// simulate a success status with data
const mockSuccess = (data) =>
    Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve(data)
    });

// simulate an error status
const mockError = (status) =>
    Promise.resolve({
        ok: false,
        status,
        json: () => Promise.resolve({})
    });

// clean the history of mockFetch
beforeEach(() => mockFetch.mockClear());


describe('apiClient', () => {

    describe('getGenres', () => {

        it('calls the correct URL without X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ genres: [] }));

            await apiClient.getGenres();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/genres`,
                undefined
            );
        });

        it('returns data on success', async () => {
            const mockData = { genres: [{ genreId: 'ROMANCE', name: 'Romance' }] };
            mockFetch.mockReturnValueOnce(mockSuccess(mockData));

            const result = await apiClient.getGenres();

            expect(result).toEqual(mockData);
        });

    });

    describe('getMyLists', () => {

        it('calls the correct URL with X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ lists: [] }));

            await apiClient.getMyLists();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-lists/`,
                expect.objectContaining({
                    headers: expect.objectContaining({
                        'X-User-Id': USER_ID
                    })
                })
            );
        });

        it('throws error on 400', async () => {
            mockFetch.mockReturnValueOnce(mockError(400));

            await expect(apiClient.getMyLists()).rejects.toThrow('400');
        });

    });

    describe('getLibraryItem', () => {

        it('extracts the path from the full href and calls with X-User-Id', async () => {
            const mockData = {
                authorName: 'George Orwell',
                identifier: null,
                publicationType: 'BOOK'
            };
            mockFetch.mockReturnValueOnce(mockSuccess(mockData));

            const fullHref = 'http://localhost:8081/my-library/3C5D126F8B';
            await apiClient.getLibraryItem(fullHref);

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-library/3C5D126F8B`,
                expect.objectContaining({
                    headers: expect.objectContaining({
                        'X-User-Id': USER_ID
                    })
                })
            );
        });

        it('returns item details on success', async () => {
            const mockData = {
                authorName: 'George Orwell',
                publicationType: 'BOOK',
                identifier: null
            };
            mockFetch.mockReturnValueOnce(mockSuccess(mockData));

            const result = await apiClient.getLibraryItem('http://localhost:8081/my-library/3C5D126F8B');

            expect(result).toEqual(mockData);
        });

        it('throws error on 404', async () => {
            mockFetch.mockReturnValueOnce(mockError(404));

            await expect(
                apiClient.getLibraryItem('http://localhost:8081/my-library/INVALID')
            ).rejects.toThrow('404');
        });

    });

    describe('getLibrary', () => {

        it('calls the correct URL with X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ items: [] }));

            await apiClient.getLibrary();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-library`,
                expect.objectContaining({
                    headers: expect.objectContaining({
                        'X-User-Id': USER_ID
                    })
                })
            );
        });

        it('returns empty list without breaking', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ items: [] }));

            const result = await apiClient.getLibrary();

            expect(result.items).toEqual([]);
        });

    });

    describe('createList', () => {

        it('sends POST with body and X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ listId: 'LOI-001' }));

            const body = { name: 'Sci-fi books', genreId: 'SCIENCE FICTION' };
            await apiClient.createList(body);

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-lists/`,
                expect.objectContaining({
                    method: 'POST',
                    headers: expect.objectContaining({
                        'Content-Type': 'application/json',
                        'X-User-Id': USER_ID
                    }),
                    body: JSON.stringify(body)
                })
            );
        });

        it('throws error on 422', async () => {
            mockFetch.mockReturnValueOnce(mockError(422));

            await expect(
                apiClient.createList({ name: '', genreId: '' })
            ).rejects.toThrow('422');
        });

    });

    describe('shareList', () => {

        it('sends PATCH to the href from _links', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ listId: 'LOI-001' }));

            const href = '/my-lists/LOI-001/share';
            const body = { shared: true };
            await apiClient.shareList(href, body);

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}${href}`,
                expect.objectContaining({
                    method: 'PATCH',
                    body: JSON.stringify(body)
                })
            );
        });

    });

    describe('createDirectSales', () => {

        it('sends POST with body and X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ saleId: 'DS-001' }));

            const body = { libraryItemId: 'ITM-001', price: 12.50 };
            await apiClient.createDirectSales(body);

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/direct-sales`,
                expect.objectContaining({
                    method: 'POST',
                    body: JSON.stringify(body)
                })
            );
        });

    });

});