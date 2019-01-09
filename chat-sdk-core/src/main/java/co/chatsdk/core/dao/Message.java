package co.chatsdk.core.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your token includes here

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.chatsdk.core.interfaces.CoreEntity;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.StorageManager;
import co.chatsdk.core.types.MessageSendStatus;
import co.chatsdk.core.types.MessageType;
import co.chatsdk.core.types.ReadStatus;
import co.chatsdk.core.utils.DaoDateTimeConverter;

@Entity
public class Message implements CoreEntity {

    @Id private Long id;

    @Unique private String entityID;

    @Convert(converter = DaoDateTimeConverter.class, columnType = Long.class)
    private DateTime date;
    private Boolean read;
    private Integer type;
    private Integer status;
    private Long senderId;
    private Long threadId;
    private Long nextMessageId;
    private Long lastMessageId;

    @ToMany(referencedJoinProperty = "messageId")
    private List<ReadReceiptUserLink> readReceiptLinks;

    @ToOne(joinProperty = "senderId")
    private User sender;

    @ToOne(joinProperty = "threadId")
    private Thread thread;

    @ToOne(joinProperty = "nextMessageId")
    private Message nextMessage;

    @ToOne(joinProperty = "lastMessageId")
    private Message lastMessage;

    @ToMany(referencedJoinProperty = "messageId")
    private List<MessageMetaValue> metaValues;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 859287859)
    private transient MessageDao myDao;

    @Generated(hash = 842349170)
    public Message(Long id, String entityID, DateTime date, Boolean read, Integer type, Integer status,
            Long senderId, Long threadId, Long nextMessageId, Long lastMessageId) {
        this.id = id;
        this.entityID = entityID;
        this.date = date;
        this.read = read;
        this.type = type;
        this.status = status;
        this.senderId = senderId;
        this.threadId = threadId;
        this.nextMessageId = nextMessageId;
        this.lastMessageId = lastMessageId;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    @Generated(hash = 1974258785)
    private transient Long thread__resolvedKey;

    @Generated(hash = 880682693)
    private transient Long sender__resolvedKey;

    @Generated(hash = 992601680)
    private transient Long nextMessage__resolvedKey;

    @Generated(hash = 88977546)
    private transient Long lastMessage__resolvedKey;

    public boolean isRead() {
        ReadStatus status = readStatusForUser(ChatSDK.currentUser());
        if (status != null && status.is(ReadStatus.read())) {
            return true;
        }
        else if (sender != null && sender.isMe()) {
            return true;
        }
        else if (read == null) {
            return false;
        }
        else {
            return read;
        }
    }

    @Override
    public String toString() {
        return String.format("Message, id: %s, type: %s, Sender: %s", id, type, getSender());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityID() {
        return this.entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public DateTime getDate() {
        return this.date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public HashMap<String, Object> getMetaValuesAsMap() {
        HashMap<String, Object> values = new HashMap<>();
        for (MessageMetaValue v : getMetaValues()) {
            values.put(v.getKey(), v.getValue());
        }
        return values;
    }

    public void setMetaValues(HashMap<String, Object> json) {
        for (String key : json.keySet()) {
            setMetaValue(key, json.get(key));
        }
    }

    protected void setMetaValue(String key, Object value) {
        MessageMetaValue metaValue = (MessageMetaValue) metaValue(key);
        if (metaValue == null) {
            metaValue = StorageManager.shared().createEntity(MessageMetaValue.class);
            metaValue.setMessageId(this.getId());
            getMetaValues().add(metaValue);
        }
        metaValue.setValue(MetaValueHelper.toString(value));
        metaValue.setKey(key);
        metaValue.update();
        update();
    }

    protected MetaValue metaValue (String key) {
        ArrayList<MetaValue> values = new ArrayList<>();
        values.addAll(getMetaValues());
        return MetaValueHelper.metaValueForKey(key, values);
    }

    public Object valueForKey (String key) {
        MetaValue value = metaValue(key);
        if (value != null && value.getValue() != null) {
            return MetaValueHelper.toObject(value.getValue());
        } else {
            return null;
        }
    }

    public String stringForKey (String key) {
        Object value = valueForKey(key);
        if (value instanceof String) {
            return (String) value;
        }
        else {
            return value.toString();
        }
    }

    public Double doubleForKey (String key) {
        Object value = valueForKey(key);
        if (value instanceof Double) {
            return (Double) value;
        }
        else {
            return (double) 0;
        }
    }

    public ReadReceiptUserLink linkForUser (User user) {
        for(ReadReceiptUserLink link : getReadReceiptLinks()) {
            User linkUser = link.getUser();
            if (linkUser != null && user != null) {
                if(linkUser.equals(user)) {
                    return link;
                }
            }
        }
        return null;
    }

    public void setUserReadStatus (User user, ReadStatus status, DateTime date) {
        ReadReceiptUserLink link = linkForUser(user);
        if(link == null) {
            link = StorageManager.shared().createEntity(ReadReceiptUserLink.class);
            link.setMessageId(this.getId());
            getReadReceiptLinks().add(link);
        }
        link.setUser(user);
        link.setStatus(status.getValue());
        link.setDate(date);

        link.update();
        update();
    }

    public LatLng getLocation () {
        Double latitude = doubleForKey(Keys.MessageLatitude);
        Double longitude = doubleForKey(Keys.MessageLongitude);
        return new LatLng(latitude, longitude);
    }

    public void setValueForKey (Object payload, String key) {
        setMetaValue(key, payload);
    }

    public String getText() {
        return stringForKey(Keys.MessageText);
    }

    public void setText(String text) {
        setValueForKey(text, Keys.MessageText);
    }

    public Integer getType () {
        return this.type;
    }

    public MessageType getMessageType() {
        if(this.type != null) {
            return MessageType.values()[this.type];
        }
        return MessageType.None;
    }

    public boolean messageTypeIs (MessageType... types) {
        for (MessageType type : types) {
            if (getMessageType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public void setMessageType(MessageType type) {
        this.type = type.ordinal();
    }

    public Integer getStatus() {
        return this.status;
    }
    public MessageSendStatus getMessageStatus() {
        if(this.status != null) {
            return MessageSendStatus.values()[this.status];
        }
        return MessageSendStatus.None;
    }

    public void setMessageStatus(MessageSendStatus status) {
        this.status = status.ordinal();
    }
    public void setStatus(Integer status) {
        this.status = status;
    }


    public Long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public ReadStatus readStatusForUser (User user) {
        return readStatusForUser(user.getEntityID());
    }

    public ReadStatus readStatusForUser (String userEntityID) {
        for(ReadReceiptUserLink link : getReadReceiptLinks()) {
            if(link.getUser() != null && link.getUser().getEntityID().equals(userEntityID)) {
                return new ReadStatus(link.getStatus());
            }
        }
        return ReadStatus.notSet();
    }

    public ReadStatus getReadStatus () {
        int total = 0;
        int userCount = getReadReceiptLinks().size();
        for(ReadReceiptUserLink link : getReadReceiptLinks()) {
            total += link.getStatus();
        }
        int status = ReadStatus.None;

        if(total >= ReadStatus.Delivered * userCount) {
            status = ReadStatus.Delivered;
        }
        if(total >= ReadStatus.Read * userCount) {
            status = ReadStatus.Read;
        }
        if (total == 0) {
            status = ReadStatus.None;
        }
        return new ReadStatus(status);

    }

    public Long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1145839495)
    public User getSender() {
        Long __key = this.senderId;
        if (sender__resolvedKey == null || !sender__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User senderNew = targetDao.load(__key);
            synchronized (this) {
                sender = senderNew;
                sender__resolvedKey = __key;
            }
        }
        return sender;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1434008871)
    public void setSender(User sender) {
        synchronized (this) {
            this.sender = sender;
            senderId = sender == null ? null : sender.getId();
            sender__resolvedKey = senderId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1483947909)
    public Thread getThread() {
        Long __key = this.threadId;
        if (thread__resolvedKey == null || !thread__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ThreadDao targetDao = daoSession.getThreadDao();
            Thread threadNew = targetDao.load(__key);
            synchronized (this) {
                thread = threadNew;
                thread__resolvedKey = __key;
            }
        }
        return thread;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1938921797)
    public void setThread(Thread thread) {
        synchronized (this) {
            this.thread = thread;
            threadId = thread == null ? null : thread.getId();
            thread__resolvedKey = threadId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 747015224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2025183823)
    public List<ReadReceiptUserLink> getReadReceiptLinks() {
        if (readReceiptLinks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReadReceiptUserLinkDao targetDao = daoSession.getReadReceiptUserLinkDao();
            List<ReadReceiptUserLink> readReceiptLinksNew = targetDao
                    ._queryMessage_ReadReceiptLinks(id);
            synchronized (this) {
                if (readReceiptLinks == null) {
                    readReceiptLinks = readReceiptLinksNew;
                }
            }
        }
        return readReceiptLinks;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 273652628)
    public synchronized void resetReadReceiptLinks() {
        readReceiptLinks = null;
    }

    public Long getNextMessageId() {
        return this.nextMessageId;
    }

    public void setNextMessageId(Long nextMessageId) {
        this.nextMessageId = nextMessageId;
    }

    public Long getLastMessageId() {
        return this.lastMessageId;
    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 871948279)
    public Message getNextMessage() {
        Long __key = this.nextMessageId;
        if (nextMessage__resolvedKey == null || !nextMessage__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message nextMessageNew = targetDao.load(__key);
            synchronized (this) {
                nextMessage = nextMessageNew;
                nextMessage__resolvedKey = __key;
            }
        }
        return nextMessage;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1912932494)
    public void setNextMessage(Message nextMessage) {
        synchronized (this) {
            this.nextMessage = nextMessage;
            nextMessageId = nextMessage == null ? null : nextMessage.getId();
            nextMessage__resolvedKey = nextMessageId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1697405005)
    public Message getLastMessage() {
        Long __key = this.lastMessageId;
        if (lastMessage__resolvedKey == null || !lastMessage__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message lastMessageNew = targetDao.load(__key);
            synchronized (this) {
                lastMessage = lastMessageNew;
                lastMessage__resolvedKey = __key;
            }
        }
        return lastMessage;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 944284900)
    public void setLastMessage(Message lastMessage) {
        synchronized (this) {
            this.lastMessage = lastMessage;
            lastMessageId = lastMessage == null ? null : lastMessage.getId();
            lastMessage__resolvedKey = lastMessageId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2015206446)
    public List<MessageMetaValue> getMetaValues() {
        if (metaValues == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageMetaValueDao targetDao = daoSession.getMessageMetaValueDao();
            List<MessageMetaValue> metaValuesNew = targetDao._queryMessage_MetaValues(id);
            synchronized (this) {
                if (metaValues == null) {
                    metaValues = metaValuesNew;
                }
            }
        }
        return metaValues;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 365870950)
    public synchronized void resetMetaValues() {
        metaValues = null;
    }

}
