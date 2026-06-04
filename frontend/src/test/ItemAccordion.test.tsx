import {fireEvent, render, screen} from '@/test-utils';
import {ItemAccordion} from '../components/accordion/ItemAccordion';

const mockItems = [{
    itemId: 'ITM-001',
    title: 'The War of the Worlds',
    picture: '/book-cover.jpg',
    links: [
        {
            rel: 'self',
            href: 'http://localhost:8081/my-library/ITM-001'
        }]
},
{
    itemId: 'ITM-002',
    title: 'Duna',
    picture: null,
    links: [
        {
            rel: 'self',
            href: 'http://localhost:8081/my-library/ITM-002'
        }
    ]
}];

const mockDetails = {
    'ITM-001': {
        authorName: 'H.G. Wells',
        identifier: '9781784872113',
        publicationType: 'BOOK'
    }
};

describe('ItemAccordion', () => {

    it('shows all the titles in the list', () => {
        render(<ItemAccordion
        items={mockItems}
        details={mockDetails}
        dispatch={vi.fn()} />);

        expect(screen.getByText('The War of the Worlds')).toBeInTheDocument();
        expect(screen.getByText('Duna')).toBeInTheDocument();
    });

    it('does not show the details before clicking', () => {
        render(<ItemAccordion
            items={mockItems}
            details={{}}
            dispatch={vi.fn()} />);

        expect(screen.queryByText('Type')).not.toBeInTheDocument();
        expect(screen.queryByText('Author')).not.toBeInTheDocument();
        expect(screen.queryByText('ISBN')).not.toBeInTheDocument();
    });

    it('render without errors when the list is empty', () => {
        render(<ItemAccordion items={[]} details={{}} dispatch={vi.fn()} />);

        expect(screen.queryByText('Type')).not.toBeInTheDocument();
    });

    it('shows the image with the correct alt', () => {
        render(<ItemAccordion
        items={mockItems}
        details={mockDetails}
        dispatch={vi.fn()} />);

        const img = screen.getByAltText('The War of the Worlds');
        expect(img).toBeInTheDocument();
    });

    it('renders item without image', () => {

        const itemsWithNullPicture = [
            {
                ...mockItems[0],
                picture: null
            },
            mockItems[1]
        ];

        render(<ItemAccordion items={itemsWithNullPicture} details={{}} dispatch={vi.fn()} />);

        expect(screen.getByText('The War of the Worlds')).toBeInTheDocument();
    });

    it('renders item details when available', () => {

        render(
            <ItemAccordion
                items={mockItems}
                details={mockDetails}
                dispatch={vi.fn()}
            />
        );

        fireEvent.click(
            screen.getByText('The War of the Worlds')
        );

        expect(
            screen.getByText('H.G. Wells')
        ).toBeInTheDocument();

        expect(
            screen.getByText('9781784872113')
        ).toBeInTheDocument();
    });

    it('does nothing when accordion is closed', () => {
        render(<ItemAccordion items={mockItems} details={{}} dispatch={vi.fn()} />);

        const accordion = screen.getByText('The War of the Worlds');

        fireEvent.click(accordion);
        fireEvent.click(accordion);

        expect(screen.getByText('The War of the Worlds')).toBeInTheDocument();
    });

    it('does not call API when item has no href', () => {
        const itemsNoHref = [{
            itemId: 'ITM-003',
            title: 'No Link Book',
            picture: null,
            links: []
        }];

        const dispatch = vi.fn();

        render(<ItemAccordion items={itemsNoHref} details={{}} dispatch={dispatch} />);

        fireEvent.click(screen.getByText('No Link Book'));

        expect(dispatch).not.toHaveBeenCalled();
    });

    it('does not fetch detail if already cached', () => {
        const dispatch = vi.fn();

        render(
            <ItemAccordion
                items={mockItems}
                details={{
                    'ITM-001': {
                        authorName: 'H.G. Wells',
                        identifier: '123',
                        publicationType: 'BOOK'
                    }
                }}
                dispatch={dispatch}
            />
        );

        fireEvent.click(screen.getByText('The War of the Worlds'));

        expect(dispatch).not.toHaveBeenCalled();
    });

    it('renders fallback image when picture is null', () => {
        render(
            <ItemAccordion
                items={mockItems}
                details={{}}
                dispatch={vi.fn()}
            />
        );

        const img = screen.getAllByRole('img')[1];
        expect(img).toBeInTheDocument();
    });
});