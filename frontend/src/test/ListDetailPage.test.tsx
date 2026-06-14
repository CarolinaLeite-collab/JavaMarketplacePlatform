import { render, screen, waitFor } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { Route, Routes } from 'react-router-dom';
import AppContext from '../context/AppContext';
import ListDetailPage from '../pages/ListDetail/ListDetailPage';
import { removeItemFromList } from '../context/lists/ListsActions';
import { apiClient } from '../services/apiClient';

vi.mock('../context/lists/ListsActions', () => ({
    removeItemFromList: vi.fn(),
}));

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getItemById: vi.fn(),
    },
}));

const mockDispatch = vi.fn();

const mockList = {
    listId: 'list-1',
    name: 'Pedro Favourites',
    genre: 'Arts',
    visibility: 'private',
    sharedUntil: null,
    links: [
        { rel: 'delete', href: '/my-lists/list-1' },
        { rel: 'remove-item', href: '/my-lists/list-1/items' },
    ],
    itemIds: ['item-1', 'item-2'],
};

const mockContextValue = {
    state: {
        app: {
            myListsHref: 'http://localhost:8081/my-lists',
            libraryHref: 'http://localhost:8081/items/my-library',
        },
        lists: {
            lists: [mockList],
            genres: [],
            error: null,
            loading: false,
        },
        sales: {
            libraryItems: [],
            error: null,
            successMessage: null,
        },
    },
    dispatch: mockDispatch,
};

function renderComponent(contextValue = mockContextValue) {
    return render(
        <AppContext.Provider value={contextValue}>
            <Routes>
                <Route path="/my-lists/:listId" element={<ListDetailPage />} />
            </Routes>
        </AppContext.Provider>,
        { initialEntries: ['/my-lists/list-1'] }
    );
}

describe('ListDetailPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        (apiClient.getItemById as any).mockResolvedValue({
            title: 'A Pattern Language',
            authorName: 'Cristopher Alexander',
            identifier: '0195019199',
            publicationTypeName: 'BOOK',
            picture: 'http://example.com/pattern.jpg',
        });
    });

    it('renders list name and subtitle', async () => {
        renderComponent();

        await waitFor(() => {
            expect(screen.getByText('Pedro Favourites')).toBeInTheDocument();
            expect(screen.getByText(/arts/i)).toBeInTheDocument();
            expect(screen.getByText(/private/i)).toBeInTheDocument();
        });
    });

    it('renders items after loading', async () => {
        renderComponent();

        await waitFor(() => {
            expect(screen.getAllByText('A Pattern Language')).toHaveLength(2);
        });
    });

    it('renders cover image for items with picture', async () => {
        renderComponent();

        await waitFor(() => {
            const images = screen.getAllByAltText('A Pattern Language');
            expect(images.length).toBeGreaterThan(0)
            expect(images[0]).toHaveAttribute('src', 'http://example.com/pattern.jpg');
        });
    });

    it('shows "No items in this list" when list has no items', async () => {
        const emptyContext = {
            ...mockContextValue,
            state: {
                ...mockContextValue.state,
                lists: {
                    ...mockContextValue.state.lists,
                    lists: [{ ...mockList, itemIds: [] }],
                },
            },
        };

        renderComponent(emptyContext);

        await waitFor(() => {
            expect(screen.getByText(/no items in this list/i)).toBeInTheDocument();
        });
    });

    it('calls removeItemFromList when remove button is clicked', async () => {
        const user = userEvent.setup();
        (removeItemFromList as any).mockResolvedValue(undefined);

        renderComponent();

        await waitFor(() => {
            expect(screen.getAllByText('A Pattern Language')).toHaveLength(2);
        });

        const removeButtons = screen.getAllByRole('button', { name: /remove item/i });
        await user.click(removeButtons[0]);

        expect(removeItemFromList).toHaveBeenCalledWith(
            mockDispatch,
            mockList.links,
            'item-1',
            'list-1'
        );
    });

    it('navigates back to /my-lists when back button is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();

        await user.click(screen.getByRole('button', { name: /back/i }));

        expect(screen.queryByText('Pedro Favourites')).not.toBeInTheDocument();
    });
});