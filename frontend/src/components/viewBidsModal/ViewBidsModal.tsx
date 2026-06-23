import {Divider, Modal, ScrollArea, Table, Text} from "@mantine/core"

interface BidDetail {
    bidId?: string;
    bidderId: string;
    bidValue: number;
    currency?: string;
    time: string;
}

interface ViewBidsModalProps {
    opened: boolean;
    bids: BidDetail [];
    onClose?: () => void;
}

function bidderUsernameFromEmail(email?: string) {
    if (!email) return 'unknown';
    return email.split('@')[0];
}

export function ViewBidsModal({opened, bids, onClose}: ViewBidsModalProps) {
    if(!bids) return null;

    return (
        <Modal
        opened={opened}
        onClose={onClose}
        title = "Bid history"
        centered
        size="lg"
        overlayProps={{backgroundOpacity: 0.55, blur: 4}}
        closeButtonProps={{"aria-label": "Close"}}
        >
            <Divider/>
            {bids.length === 0 ? (
                <Text size="sm" c="dimmed">
                    No bids have been placed yet.
                </Text>
            ) : (
                <ScrollArea h={300}>
                    <Table withTableBorder withColumnBorders striped highlightOnHover>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th>Bidder</Table.Th>
                                <Table.Th>Amount</Table.Th>
                                <Table.Th>Time</Table.Th>
                            </Table.Tr>
                        </Table.Thead>

                        <Table.Tbody>
                            {bids.map((bid, index) => (
                                <Table.Tr key={bid.bidId ?? `${bid.bidderId}-${bid.time}-${index}`}>
                                    <Table.Td>{bidderUsernameFromEmail(bid.bidderId)}</Table.Td>
                                    <Table.Td>
                                        {bid.bidValue} {bid.currency ?? 'EUR'}
                                    </Table.Td>
                                    <Table.Td>{new Date(bid.time).toLocaleString()}</Table.Td>
                                </Table.Tr>
                            ))}
                        </Table.Tbody>
                    </Table>
                </ScrollArea>
            )}
        </Modal>
    )
}