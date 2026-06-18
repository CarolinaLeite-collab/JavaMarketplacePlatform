import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import {ListsTabs} from '../../components/lists/ListsTabs.tsx';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const renderAt = (path: string) =>
    render(
        <MemoryRouter initialEntries={[path]}>
            <MantineProvider>
                <Routes>
                    <Route path="*" element={<ListsTabs />} />
                </Routes>
            </MantineProvider>
        </MemoryRouter>
    );

describe('ListsTabs – rendering', () => {
    it('renders both tab labels', () => {
        renderAt('/lists/my-lists');
        expect(screen.getByRole('tab', { name: /my lists/i })).toBeInTheDocument();
        expect(screen.getByRole('tab', { name: /public lists/i })).toBeInTheDocument();
    });
});

describe('ListsTabs – active tab detection', () => {
    it('marks "My lists" as active on /lists/my-lists', () => {
        renderAt('/lists/my-lists');
        expect(screen.getByRole('tab', { name: /my lists/i })).toHaveAttribute('aria-selected', 'true');
        expect(screen.getByRole('tab', { name: /public lists/i })).toHaveAttribute('aria-selected', 'false');
    });

    it('marks "Public lists" as active on /lists/public', () => {
        renderAt('/lists/public');
        expect(screen.getByRole('tab', { name: /public lists/i })).toHaveAttribute('aria-selected', 'true');
        expect(screen.getByRole('tab', { name: /my lists/i })).toHaveAttribute('aria-selected', 'false');
    });

    it('defaults to "My lists" as active on an unrelated path', () => {
        renderAt('/lists');
        expect(screen.getByRole('tab', { name: /my lists/i })).toHaveAttribute('aria-selected', 'true');
    });
});

describe('ListsTabs – navigation', () => {
    it('navigates to /lists/public when clicking "Public lists"', async () => {
        const user = userEvent.setup();
        renderAt('/lists/my-lists');
        await user.click(screen.getByRole('tab', { name: /public lists/i }));
        expect(mockNavigate).toHaveBeenCalledWith('/lists/public');
    });

    it('navigates to /lists/my-lists when clicking "My lists"', async () => {
        const user = userEvent.setup();
        renderAt('/lists/public');
        await user.click(screen.getByRole('tab', { name: /my lists/i }));
        expect(mockNavigate).toHaveBeenCalledWith('/lists/my-lists');
    });
});
