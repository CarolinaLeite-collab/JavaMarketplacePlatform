import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { MantineProvider } from '@mantine/core';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { useUser } from '../context/UserContext';
import AuctionDetailPage from '../pages/AuctionDetail/AuctionDetailPage';

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');
    return {
        ...actual,
        useUser: vi.fn(),
    };
});

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
    });

    it('renders publication type and title', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Book:/)).toBeInTheDocument();
        expect(screen.getByText('1984')).toBeInTheDocument();
    });

    it('renders highest bid', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/75/)).toBeInTheDocument();
    });

    it('renders starting price', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Starting price: 50 EUR/)).toBeInTheDocument();
    });

    it('renders number of bids', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/6 bids/)).toBeInTheDocument();
    });

    it('renders seller', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Sold by Unknown/)).toBeInTheDocument();
    });

    it('renders deadline', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Ends in 3 days/)).toBeInTheDocument();
    });

    it('renders status badge', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Active')).toBeInTheDocument();
    });

    it('renders seller description', async () => {
        renderAuctionDetail();

        expect(await screen.findByText("Seller's description:")).toBeInTheDocument();
        expect(screen.getByText(/First edition of George Orwell/)).toBeInTheDocument();
    });

    it('renders quick info cards', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Author')).toBeInTheDocument();
        expect(screen.getByText('George Orwell')).toBeInTheDocument();
        expect(screen.getByText('Edition')).toBeInTheDocument();
        expect(screen.getByText('1st Edition')).toBeInTheDocument();
        expect(screen.getByText('ISBN')).toBeInTheDocument();
        expect(screen.getByText('978-0-451-52493-5')).toBeInTheDocument();
        expect(screen.getByText('Condition')).toBeInTheDocument();
        expect(screen.getByText('Good')).toBeInTheDocument();
    });

    it('renders synopsis section', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Synopsis:')).toBeInTheDocument();
        expect(screen.getByText(/seminal texts/)).toBeInTheDocument();
    });

    it('renders details table with publisher and genre', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Publisher')).toBeInTheDocument();
        expect(screen.getByText('Secker & Warburg')).toBeInTheDocument();
        const genreLabels = screen.getAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
    });

    it('renders details table with year and language', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Year')).toBeInTheDocument();
        expect(screen.getByText('1949')).toBeInTheDocument();
        expect(screen.getByText('English')).toBeInTheDocument();
    });

    it('renders details table with pages and binding', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Pages')).toBeInTheDocument();
        expect(screen.getByText('328')).toBeInTheDocument();
        expect(screen.getByText('Hardcover')).toBeInTheDocument();
    });

    it('renders details table with weight and dimensions', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Weight')).toBeInTheDocument();
        expect(screen.getByText('350 g')).toBeInTheDocument();
        expect(screen.getByText('20 x 13 x 2.5 cm')).toBeInTheDocument();
    });

    it('renders Place Bid button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeInTheDocument();
    });

    it('renders Buy Now button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /buy now/i })).toBeInTheDocument();
    });

    it('renders Add to Cart button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /add to cart/i })).toBeInTheDocument();
    });
});