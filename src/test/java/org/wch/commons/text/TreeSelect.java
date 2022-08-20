package org.wch.commons.text;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wch.commons.beans.TreeNode;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2022-07-13 15:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeSelect extends TreeNode<TreeSelect> {

    private String path;

    private Integer childrenCount;

    public TreeSelect(Long id, String label, Long parentId) {
        super(id, label, parentId);
    }

    public TreeSelect(String uid, String label, String parentUid) {
        super(uid, label, parentUid);
    }

}
