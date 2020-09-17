package com.koodo.eblog.im.handler.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcludeMineChannelContextFilter implements ChannelContextFilter {

    private ChannelContext currentContext;

    @Override
    public boolean filter(ChannelContext channelContext) {
        return !currentContext.userid.equals(channelContext.userid);
    }

}
