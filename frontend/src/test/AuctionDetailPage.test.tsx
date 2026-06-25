import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { MantineProvider } from '@mantine/core';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { useUser } from '../context/UserContext';
import AuctionDetailPage from '../pages/AuctionDetail/AuctionDetailPage';
import { apiClient } from '../services/apiClient';

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');
    return { ...actual, useUser: vi.fn() };
});

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getAuctionById: vi.fn(),
        getItemById: vi.fn(),
        getEditionById: vi.fn(),
        getPublishingCompanyById: vi.fn(),
        getPublicationById: vi.fn(),
        getByHref: vi.fn(),
        postByHref: vi.fn(),
    },
}));

const mockAuction = {
    auctionId: 'test-123',
    itemIds: ['ITEM-001'],
    startingPrice: 20.0,
    reservePrice: 30.0,
    outrightPrice: 50.0,
    currentPrice: 28.5,
    priceCurrency: 'EUR',
    startDate: '2026-06-01T00:00:00Z',
    endDate: '2027-06-30T00:00:00Z',
    seller: 'pedro@aeiou.com',
    _links: {
        bids: { href: 'http://localhost:8081/auctions/test-123/bids' },
    },
};

const mockBidsResponse = {
    _embedded: {
        bids: [
            {
                bidId: 'BID-001',
                buyerId: 'pedro@aeiou.com',
                bidValue: 28.5,
                currency: 'EUR',
                bidDate: '2026-06-22T18:39:05Z',
            },
            {
                bidId: 'BID-002',
                buyerId: 'maria@aeiou.com',
                bidValue: 25,
                currency: 'EUR',
                bidDate: '2026-06-22T18:10:00Z',
            },
            {
                bidId: 'BID-003',
                buyerId: 'joao@aeiou.com',
                bidValue: 22,
                currency: 'EUR',
                bidDate: '2026-06-22T17:50:00Z',
            },
        ],
    },
};

const mockItem = {
    itemId: 'ITEM-001',
    editionId: 'ED-001',
    condition: 'GOOD',
    description: 'Paperback in good condition. Essential reading on urban density.',
    saleStatus: 'NotOnSale',
    picture: 'https://example.com/cover.jpg',
    identifier: '9780471989677',
    language: 'ENGLISH',
    publishingYear: 2005,
    publicationTypeName: 'BOOK',
    title: 'Spacematrix: Space, Density and Urban Form',
    authorName: 'Meta Berghauser Pont',
    releaseYear: 2005,
    genreName: 'Arts',
};

const mockEdition = {
    editionId: 'ED-001',
    publishingCompanyId: 'PC-001',
    publicationId: 'PUB-001',
    numberOfPages: 320,
    editionNumber: 1,
    binding: 'PAPERBACK',
    weight: { value: 794, unit: 'GRAMS' },
    dimension: { width: 17.0, height: 24.0, thickness: 2.5, unit: 'CENTIMETERS' },
};

const mockPublisher = {
    publishingCompanyId: 'PC-001',
    publishingCompanyName: 'naiOIO Publishers',
};

const mockPublication = {
    publicationId: 'PUB-001',
    synopsis: 'A rigorous study of density, space, and urban form.',
};

function renderAuctionDetail({ auctionId = 'test-123' } = {}) {
    return render(
        <MantineProvider>
            <MemoryRouter initialEntries={[`/auctions/${auctionId}`]}>
                <Routes>
                    <Route path="/auctions/:auctionId" element={<AuctionDetailPage />} />
                </Routes>
            </MemoryRouter>
        </MantineProvider>
    );
}

describe('AuctionDetailPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();

        vi.mocked(useUser).mockReturnValue({
            currentUser: 'pedro@aeiou.com',
            toggleUser: vi.fn(),
        });

        vi.mocked(apiClient.getAuctionById).mockResolvedValue(mockAuction);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);
        vi.mocked(apiClient.getEditionById).mockResolvedValue(mockEdition);
        vi.mocked(apiClient.getPublishingCompanyById).mockResolvedValue(mockPublisher);
        vi.mocked(apiClient.getPublicationById).mockResolvedValue(mockPublication);
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockBidsResponse);
        vi.mocked(apiClient.postByHref).mockResolvedValue({});
    });

    it('renders publication type and title', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Book:/)).toBeInTheDocument();
        expect(screen.getByText('Spacematrix: Space, Density and Urban Form')).toBeInTheDocument();
    });

    it('renders current price when available', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/28.5 EUR/)).toBeInTheDocument();
    });

    it('renders starting price when current price is missing', async () => {
        vi.mocked(apiClient.getAuctionById).mockResolvedValue({
            ...mockAuction,
            currentPrice: null,
        });
        vi.mocked(apiClient.getByHref).mockResolvedValue({ _embedded: { bids: [] } });

        renderAuctionDetail();

        const results = await screen.findAllByText(/20 EUR/);
        expect(results.length).toBeGreaterThanOrEqual(1);
    });

    it('renders starting price label', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Starting price: 20 EUR/)).toBeInTheDocument();
    });

    it('renders bid count button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /3 bids/i })).toBeInTheDocument();
    });

    it('renders seller username derived from email', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Sold by pedro/i)).toBeInTheDocument();
    });

    it('renders unknown seller when seller is missing', async () => {
        vi.mocked(apiClient.getAuctionById).mockResolvedValue({
            ...mockAuction,
            seller: null,
        });

        renderAuctionDetail();

        expect(await screen.findByText(/Sold by unknown/i)).toBeInTheDocument();
    });

    it('renders status badge', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Active')).toBeInTheDocument();
    });

    it('renders seller description', async () => {
        renderAuctionDetail();

        expect(await screen.findByText("Seller's description:")).toBeInTheDocument();
        expect(screen.getByText(/Essential reading on urban density/i)).toBeInTheDocument();
    });

    it('renders quick info cards', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Author')).toBeInTheDocument();
        expect(screen.getByText('Meta Berghauser Pont')).toBeInTheDocument();
        expect(screen.getByText('Edition')).toBeInTheDocument();
        expect(screen.getByText('1')).toBeInTheDocument();
        expect(screen.getByText('ISBN')).toBeInTheDocument();
        expect(screen.getByText('9780471989677')).toBeInTheDocument();
        expect(screen.getByText('Condition')).toBeInTheDocument();
        expect(screen.getByText('GOOD')).toBeInTheDocument();
    });

    it('renders details table with publisher', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Publisher')).toBeInTheDocument();
        expect(screen.getByText('naiOIO Publishers')).toBeInTheDocument();
    });

    it('renders details table with genre', async () => {
        renderAuctionDetail();

        const genreLabels = await screen.findAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
        expect(screen.getByText('Arts')).toBeInTheDocument();
    });

    it('renders details table with year and language', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Year')).toBeInTheDocument();
        expect(screen.getByText('2005')).toBeInTheDocument();
        expect(screen.getByText('ENGLISH')).toBeInTheDocument();
    });

    it('renders details table with pages and binding', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Pages')).toBeInTheDocument();
        expect(screen.getByText('320')).toBeInTheDocument();
        expect(screen.getByText('PAPERBACK')).toBeInTheDocument();
    });

    it('renders details table with weight', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Weight')).toBeInTheDocument();
        expect(screen.getByText('794 GRAMS')).toBeInTheDocument();
    });

    it('renders details table with dimensions', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Dimensions')).toBeInTheDocument();
        expect(screen.getByText('17 x 24 x 2.5 CENTIMETERS')).toBeInTheDocument();
    });

    it('renders synopsis from publication when available', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Synopsis:')).toBeInTheDocument();
        expect(screen.getByText('A rigorous study of density, space, and urban form.')).toBeInTheDocument();
    });

    it('renders synopsis fallback when publication fails to load', async () => {
        vi.mocked(apiClient.getPublicationById).mockRejectedValue(new Error('404'));

        renderAuctionDetail();

        expect(await screen.findByText('Synopsis:')).toBeInTheDocument();
        expect(screen.getByText('N/A')).toBeInTheDocument();
    });

    it('renders Place Bid button enabled for logged-in user when bids link exists and auction is active', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'ana@aeiou.com',
            toggleUser: vi.fn()
        });
        renderAuctionDetail();

        const button = await screen.findByRole('button', { name: /place bid/i });
        expect(button).toBeInTheDocument();
        expect(button).toBeEnabled();
    });

    it('disables Place Bid for guest user', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeDisabled();
    });

    it('disables Place Bid for auction owner', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'pedro@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeDisabled();
    });

    it('disables Place Bid for non-registeed user', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeDisabled();
    });

    it('disables Place Bid when bids link is missing', async () => {
        vi.mocked(apiClient.getAuctionById).mockResolvedValue({
            ...mockAuction,
            _links: {},
        });

        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeDisabled();
    });

    it('disables Place Bid when auction is ended', async () => {
        vi.mocked(apiClient.getAuctionById).mockResolvedValue({
            ...mockAuction,
            endDate: '2020-01-01T00:00:00Z',
        });

        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeDisabled();
        expect(screen.getByText('Ended')).toBeInTheDocument();
    });

    it('shows bids count button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /3 bids/i })).toBeInTheDocument();
    });

    it('shows error state when auction fails to load', async () => {
        vi.mocked(apiClient.getAuctionById).mockRejectedValue(new Error('404'));

        renderAuctionDetail();

        expect(await screen.findByText(/auction not found\./i)).toBeInTheDocument();
    });

    it('shows N/A for edition fields when edition fails to load', async () => {
        vi.mocked(apiClient.getEditionById).mockRejectedValue(new Error('404'));

        renderAuctionDetail();

        expect(await screen.findByText('Publisher')).toBeInTheDocument();
        expect(screen.getAllByText('N/A').length).toBeGreaterThanOrEqual(1);
    });
});