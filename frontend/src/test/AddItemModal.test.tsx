import { render, screen, waitFor } from '../test-utils';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { AddItemModal } from '../components/addItemModal/AddItemModal';
import AppContext from '../context/AppContext';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        postByHref: vi.fn(),
        extractIdFromSelfLink: vi.fn(),
    },
}));

const hrefs = {
    authorsHref: 'http://localhost:8081/authors',
    genresHref: 'http://localhost:8081/genres',
    publicationTypesHref: 'http://localhost:8081/publication-types',
    publishingCompaniesHref: 'http://localhost:8081/publishingCompanies',
    createPublicationHref: 'http://localhost:8081/publications',
    createEditionHref: 'http://localhost:8081/editions',
    createItemHref: 'http://localhost:8081/items',
    libraryAddHref: 'http://localhost:8081/my-library/',
};

const renderAddItemModal = ({
                                opened = true,
                                onClose = vi.fn(),
                                onItemAdded = vi.fn(),
                                appOverrides = {},
                            } = {}) => {
    const state = {
        app: {
            ...hrefs,
            ...appOverrides,
        },
    };

    render(
        <AppContext.Provider value={{ state, dispatch: vi.fn() }}>
            <AddItemModal
                opened={opened}
                onClose={onClose}
                onItemAdded={onItemAdded}
            />
        </AppContext.Provider>
    );

    return { onClose, onItemAdded };
};

const mockOptionsRequests = () => {
    vi.mocked(apiClient.getByHref)
        .mockResolvedValueOnce([
            { authorId: 'AUTHOR-001', authorName: 'Robert C. Martin' },
        ])
        .mockResolvedValueOnce([
            { genreId: 'GENRE-001', genreName: 'Software Engineering' },
        ])
        .mockResolvedValueOnce([
            { publicationTypeId: 'TYPE-001', publicationTypeName: 'Book' },
        ])
        .mockResolvedValueOnce([
            { publishingCompanyId: 'COMPANY-001', name: 'Prentice Hall' },
        ]);
};

const fillPublicationStep = async (user: ReturnType<typeof userEvent.setup>) => {
    await user.type(screen.getByLabelText(/title/i), 'Clean Code');

    await user.click(screen.getByRole('combobox', { name: /author/i }));
    await user.click(await screen.findByText('Robert C. Martin'));

    await user.clear(screen.getByLabelText(/release year/i));
    await user.type(screen.getByLabelText(/release year/i), '2008');

    await user.click(screen.getByRole('combobox', { name: /genre/i }));
    await user.click(await screen.findByText('Software Engineering'));

    await user.click(screen.getByRole('button', { name: /next/i }));
};

const fillEditionStep = async (user: ReturnType<typeof userEvent.setup>) => {
    await user.click(screen.getByRole('combobox', { name: /publication type/i }));
    await user.click(await screen.findByText('Book'));

    await user.type(screen.getByLabelText(/isbn\/issn/i), '9780132350884');

    await user.click(screen.getByRole('combobox', { name: /language/i }));
    await user.click(await screen.findByText('ENGLISH'));

    await user.click(screen.getByRole('combobox', { name: /publishing company/i }));
    await user.click(await screen.findByText('Prentice Hall'));

    await user.clear(screen.getByLabelText(/publishing year/i));
    await user.type(screen.getByLabelText(/publishing year/i), '2008');

    await user.click(screen.getByRole('combobox', { name: /binding/i }));
    await user.click(await screen.findByText('PAPERBACK'));

    await user.clear(screen.getByLabelText(/number of pages/i));
    await user.type(screen.getByLabelText(/number of pages/i), '464');

    await user.clear(screen.getByLabelText(/edition number/i));
    await user.type(screen.getByLabelText(/edition number/i), '1');

    await user.click(screen.getByRole('button', { name: /next/i }));
};

const fillItemStep = async (user: ReturnType<typeof userEvent.setup>) => {
    await user.click(screen.getByRole('combobox', { name: /condition/i }));
    await user.click(await screen.findByText('GOOD'));

    await user.type(
        screen.getByLabelText(/description/i),
        'Used copy in good condition'
    );

    await user.type(
        screen.getByLabelText(/picture url/i),
        'https://example.com/clean-code.jpg'
    );
};

describe('AddItemModal', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        mockOptionsRequests();
    });

    it('loads options from HATEOAS links when modal opens', async () => {
        renderAddItemModal();

        await waitFor(() => {
            expect(apiClient.getByHref).toHaveBeenCalledTimes(4);
        });

        expect(apiClient.getByHref).toHaveBeenCalledWith(hrefs.authorsHref);
        expect(apiClient.getByHref).toHaveBeenCalledWith(hrefs.genresHref);
        expect(apiClient.getByHref).toHaveBeenCalledWith(hrefs.publicationTypesHref);
        expect(apiClient.getByHref).toHaveBeenCalledWith(hrefs.publishingCompaniesHref);
    });

    it('creates publication, edition, item and adds item to library', async () => {
        const user = userEvent.setup({ delay: null });
        const createdPublication = {
            _links: {
                self: {
                    href: 'http://localhost:8081/publications/PUB-001',
                },
            },
        };
        const createdEdition = {
            _links: {
                self: {
                    href: 'http://localhost:8081/editions/ED-001',
                },
            },
        };
        const createdItem = {
            itemId: 'ITEM-001',
            condition: 'GOOD',
            description: 'Used copy in good condition',
        };

        vi.mocked(apiClient.postByHref)
            .mockResolvedValueOnce(createdPublication)
            .mockResolvedValueOnce(createdEdition)
            .mockResolvedValueOnce(createdItem)
            .mockResolvedValueOnce({});

        vi.mocked(apiClient.extractIdFromSelfLink)
            .mockReturnValueOnce('PUB-001')
            .mockReturnValueOnce('ED-001');

        const { onItemAdded } = renderAddItemModal();

        await waitFor(() => {
            expect(apiClient.getByHref).toHaveBeenCalledTimes(4);
        });

        await fillPublicationStep(user);
        await fillEditionStep(user);
        await fillItemStep(user);

        await user.click(screen.getByRole('button', { name: /create item/i }));

        await waitFor(() => {
            expect(apiClient.postByHref).toHaveBeenCalledTimes(4);
        });

        expect(apiClient.postByHref).toHaveBeenNthCalledWith(
            1,
            hrefs.createPublicationHref,
            {
                title: 'Clean Code',
                authorId: 'AUTHOR-001',
                releaseYear: 2008,
                genreId: 'GENRE-001',
            }
        );

        expect(apiClient.postByHref).toHaveBeenNthCalledWith(
            2,
            `${hrefs.createEditionHref}?pubId=PUB-001`,
            {
                publicationTypeId: 'TYPE-001',
                publishingCompanyId: 'COMPANY-001',
                publishingYear: 2008,
                language: 'ENGLISH',
                identifier: '9780132350884',
                numberOfPages: 464,
                editionNumber: 1,
                binding: 'PAPERBACK',
            }
        );

        expect(apiClient.postByHref).toHaveBeenNthCalledWith(
            3,
            hrefs.createItemHref,
            {
                editionId: 'ED-001',
                condition: 'GOOD',
                description: 'Used copy in good condition',
            }
        );

        expect(apiClient.postByHref).toHaveBeenNthCalledWith(
            4,
            hrefs.libraryAddHref,
            {
                itemId: 'ITEM-001',
            }
        );

        expect(
            await screen.findByText(/item successfully registered and added to your library/i)
        ).toBeInTheDocument();

        expect(onItemAdded).not.toHaveBeenCalled();

        await user.click(screen.getByRole('button', { name: /done/i }));

        expect(onItemAdded).toHaveBeenCalledWith(createdItem);
    }, 25000);

    it('does not submit when required fields are missing', async () => {
        const user = userEvent.setup({ delay: null });
        const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});

        renderAddItemModal();

        await user.click(screen.getByRole('button', { name: /next/i }));
        await user.click(screen.getByRole('button', { name: /next/i }));
        await user.click(screen.getByRole('button', { name: /create item/i }));

        expect(alertSpy).toHaveBeenCalledWith('Please fill all required fields.');
        expect(apiClient.postByHref).not.toHaveBeenCalled();

        alertSpy.mockRestore();
    });

    it('disables create item button when required HATEOAS links are missing', async () => {
        const user = userEvent.setup({ delay: null });

        renderAddItemModal({
            appOverrides: {
                createItemHref: null,
            },
        });

        await user.click(screen.getByRole('button', { name: /next/i }));
        await user.click(screen.getByRole('button', { name: /next/i }));

        expect(screen.getByRole('button', { name: /create item/i })).toBeDisabled();
    });
});
