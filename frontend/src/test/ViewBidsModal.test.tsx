import { describe, it, expect, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen, within } from '../test-utils';
import { ViewBidsModal } from '../components/viewBidsModal/ViewBidsModal';

describe('ViewBidsModal', () => {
    const bids = [
        {
            bidId: 'BID-001',
            bidderId: 'pedro@aeiou.com',
            bidValue: 25,
            currency: 'EUR',
            time: '2026-06-22T18:39:05Z',
        },
        {
            bidId: 'BID-002',
            bidderId: 'maria@aeiou.com',
            bidValue: 30,
            currency: 'USD',
            time: '2026-06-22T19:10:00Z',
        },
    ];

    it('renders nothing when bids is falsy', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={null as any}
                onClose={vi.fn()}
            />
        );

        expect(screen.queryByText(/bid history/i)).not.toBeInTheDocument();
    });

    it('renders modal title', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={[]}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByText(/bid history/i)).toBeInTheDocument();
    });

    it('renders empty state when there are no bids', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={[]}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByText(/no bids have been placed yet\./i)).toBeInTheDocument();
        expect(screen.queryByRole('table')).not.toBeInTheDocument();
    });

    it('renders table headers when bids exist', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByRole('table')).toBeInTheDocument();
        expect(screen.getByText('Bidder')).toBeInTheDocument();
        expect(screen.getByText('Amount')).toBeInTheDocument();
        expect(screen.getByText('Time')).toBeInTheDocument();
    });

    it('renders bidder usernames derived from email', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByText('pedro')).toBeInTheDocument();
        expect(screen.getByText('maria')).toBeInTheDocument();
    });

    it('renders bid amounts and currencies', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByText(/25\s+EUR/i)).toBeInTheDocument();
        expect(screen.getByText(/30\s+USD/i)).toBeInTheDocument();
    });

    it('falls back to EUR when currency is missing', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={[
                    {
                        bidId: 'BID-003',
                        bidderId: 'ana@aeiou.com',
                        bidValue: 18,
                        time: '2026-06-22T20:00:00Z',
                    },
                ]}
                onClose={vi.fn()}
            />
        );

        expect(screen.getByText(/18\s+EUR/i)).toBeInTheDocument();
    });

    it('renders one table row per bid', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        const rows = screen.getAllByRole('row');
        expect(rows).toHaveLength(3);
    });

    it('renders formatted time for each bid', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        const expected1 = new Date('2026-06-22T18:39:05Z').toLocaleString();
        const expected2 = new Date('2026-06-22T19:10:00Z').toLocaleString();

        expect(screen.getByText(expected1)).toBeInTheDocument();
        expect(screen.getByText(expected2)).toBeInTheDocument();
    });

    it('renders row content correctly', () => {
        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={vi.fn()}
            />
        );

        const rows = screen.getAllByRole('row');
        const firstDataRow = rows[1];
        const cells = within(firstDataRow).getAllByRole('cell');

        expect(cells[0]).toHaveTextContent('pedro');
        expect(cells[1]).toHaveTextContent('25 EUR');
        expect(cells[2]).toHaveTextContent(new Date('2026-06-22T18:39:05Z').toLocaleString());
    });

    it('calls onClose when close button is clicked', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();

        render(
            <ViewBidsModal
                opened={true}
                bids={bids}
                onClose={onClose}
            />
        );

        const closeButton = screen.getByRole('button', { name: /close/i });
        await user.click(closeButton);

        expect(onClose).toHaveBeenCalledTimes(1);
    });
});