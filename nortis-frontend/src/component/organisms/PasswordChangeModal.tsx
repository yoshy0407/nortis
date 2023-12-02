import { Modal } from "@mui/material";
import React, { useState } from "react";
import PasswordChangeCard from "../molecules/PasswordChangeCard";

export interface PasswordChangeModalType {
    /**
     * モーダルのオープン
     * @returns 
     */
    show: () => void;
}

interface PasswordChangeModalProps {

}

const PasswordChangeModal = React.forwardRef<PasswordChangeModalType, PasswordChangeModalProps>((props, ref) => {
    const [open, setOpen] = useState(false);

    React.useImperativeHandle(ref, () => ({
        show() {
            setOpen(true);
        },
    }));

    const closeModal = () => { setOpen(false) }

    return (
        <Modal
            open={open}
            onClose={closeModal}
        >
            <PasswordChangeCard onCancel={closeModal} />
        </Modal>
    )
})

export default PasswordChangeModal;