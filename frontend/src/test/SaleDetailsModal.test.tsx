import { describe, it, expect, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '../test-utils';
import { SaleDetailsModal } from '../components/saleDetailsModal/SaleDetailsModal';

describe('SaleDetailsModal', () => {
    const baseItem = {
        cover: 'https://example.com/cover.jpg',
        title: 'Dune',
        author: 'Frank Herbert',
        genre: 'Science Fiction',
        condition: 'GOOD',
        price: '9.99 EUR',
        seller: 'user123',
        saleType: 'Auction' as const,
        directSaleId: null,
        auctionId: 'AU-123',
        selfHref: 'http://localhost:8081/auctions/AU-123',
    };

    it('renders nothing when item is null', () => {
        const { container } = render(
            <SaleDetailsModal
                opened={true}
                item={null}
                onClose={vi.fn()}
                onSeeMore={vi.fn()}
            />
        );

        expect(screen.queryByText(/see more/i)).not.toBeInTheDocument();
        expect(screen.queryByText(/sold by/i)).not.toBeInTheDocument();
    });

    it('renders sale details when item is provided', () => {
        render(
            <SaleDetailsModal
                opened={true}
                item={baseItem}
                onClose={vi.fn()}
                onSeeMore={vi.fn()}
            />
        );

        expect(screen.getByText('Dune')).toBeInTheDocument();
        expect(screen.getByText(/frank herbert/i)).toBeInTheDocument();
        expect(screen.getByText(/science fiction/i)).toBeInTheDocument();
        expect(screen.getByText(/auction/i)).toBeInTheDocument();
        expect(screen.getByText(/good/i)).toBeInTheDocument();
        expect(screen.getByText(/9\.99 eur/i)).toBeInTheDocument();
        expect(screen.getByText(/sold by:\s*user123/i)).toBeInTheDocument();
    });

    it('calls onClose when close button is clicked', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();

        render(
            <SaleDetailsModal
                opened={true}
                item={baseItem}
                onClose={onClose}
                onSeeMore={vi.fn()}
            />
        );

        const closeButton = screen.getByRole('button', { name: /close/i });
        await user.click(closeButton);

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    it('calls onSeeMore when button is clicked', async () => {
        const user = userEvent.setup();
        const onSeeMore = vi.fn();

        render(
            <SaleDetailsModal
                opened={true}
                item={baseItem}
                onClose={vi.fn()}
                onSeeMore={onSeeMore}
            />
        );

        await user.click(screen.getByRole('button', { name: /see more/i }));

        expect(onSeeMore).toHaveBeenCalledTimes(1);
    });
});